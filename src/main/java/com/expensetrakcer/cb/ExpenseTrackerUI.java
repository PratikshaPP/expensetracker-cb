package com.expensetrakcer.cb;


import java.lang.reflect.Field;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.form.AbstractForm.DeleteHandler;
import org.vaadin.viritin.form.AbstractForm.ResetHandler;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.annotations.Theme;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class ExpenseTrackerUI extends UI {

	private final ExpenseDetailsRepository repo;

	private final ExpenseEditor editor;

	private final MTable<ExpenseDetail> grid;
	
	private final TextField filter;

	private final Button addNewBtn;
	
	private final Button findBtn;
	
	
	
	//private NativeSelect fieldToSearch;

	//private TextField searchText;
	
	private DateField fromDate;
	
	private DateField toDate;
	
	private final Button showAllBtn;

	static final Logger log = LoggerFactory.getLogger(ExpenseTrackerUI.class);

	
	@Autowired
	public ExpenseTrackerUI(ExpenseDetailsRepository repo, ExpenseEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new MTable<>(ExpenseDetail.class).withProperties("transactionDate", "transactionAmount", "transactionType", "merchant", "description").withHeight("300px");
		this.addNewBtn = new Button("New expense", FontAwesome.PLUS_CIRCLE);
		this.filter = new TextField();
		
		//fieldToSearch = new NativeSelect ("Search By");
		//populateOptions(fieldToSearch);
		//searchText = new TextField("Search String");
		
		fromDate = new DateField("Transaction From Date");
		toDate = new DateField("To Date");
		
		this.findBtn = new Button("Search", FontAwesome.SEARCH);
		this.showAllBtn = new Button("Show All", FontAwesome.LIST);
	}

	/**
	 * 
	 * @param fieldToSearch
	 */
	private void populateOptions(NativeSelect fieldToSearch) {
		Field[] fields = ExpenseDetail.class.getDeclaredFields();
		for (Field f : fields) {
			if (!f.getName().equalsIgnoreCase("id")) {
				fieldToSearch.addItem(f.getName());
			}
		}
	}

	@Override
	protected void init(VaadinRequest request) {
		
		grid.addMValueChangeListener(new MValueChangeListener<ExpenseDetail>() {
			@Override
			public void valueChange(MValueChangeEvent<ExpenseDetail> e) {
				if (e.getValue() == null) {
					editor.setVisible(false);
				} else {
					editor.setEntity(e.getValue());
				}
			}
		});

		// Instantiate and edit new Expense the new button is clicked
		addNewBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				editor.setEntity(new ExpenseDetail("", new Date(), new Double(0.0), "", ""));
			}
		});

		// Listen changes made by the editor, refresh data from backend
		editor.setSavedHandler(new SavedHandler<ExpenseDetail>() {
			@Override
			public void onSave(ExpenseDetail ed) {
				repo.save(ed);
				listExpenses(null);
				editor.setVisible(false);
			}
		});

		editor.setResetHandler(new ResetHandler<ExpenseDetail>() {
			@Override
			public void onReset(ExpenseDetail ed) {
				editor.setVisible(false);
				listExpenses(null);
			}
		});

		editor.setDeleteHandler(new DeleteHandler<ExpenseDetail>() {
			@Override
			public void onDelete(ExpenseDetail ed) {
				repo.delete(ed);
				listExpenses(null);
			}
		});



		findBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				
				listExpensesByCriteria (null, null);
			}

		});		
		
		
		showAllBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent e) {
				listExpenses(null);
			}
		});
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn); 
		HorizontalLayout dateFields = new HorizontalLayout(fromDate, toDate);
		HorizontalLayout searchLayout = new HorizontalLayout(findBtn, showAllBtn);
		
		VerticalLayout mainLayout = new MVerticalLayout(actions, grid, editor, dateFields, searchLayout);
		setContent(mainLayout);
		
		actions.setSpacing(true);
		dateFields.setSpacing(true);
		searchLayout.setSpacing(true);

		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		filter.setInputPrompt("Filter by merchant");
		
		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent e) {
				listExpenses(e.getText());
			}
		});	
		
		
		// Initialize listing
		listExpenses(null);
	}

	private void listExpenses(String text) {
		
		if (StringUtils.isEmpty(text)) {
			grid.setBeans(repo.findAll());
		} else {
			grid.setBeans(repo.findByMerchantStartsWithIgnoreCase(text));
		}		
		
        
	}
	

	private void listExpensesByCriteria(String searchField, String searchText) {
		log.info("*********      Search Field       ************ "+ searchField);
		log.info("*********      Search Text       ************ "+ searchText);
		if (searchField.equalsIgnoreCase("merchant")) {
			grid.setBeans(repo.findByMerchant(searchText));
		} else {
			log.info("*********  Query By Search Field       ************ "+ searchField);
		}
		
		
	}
}
