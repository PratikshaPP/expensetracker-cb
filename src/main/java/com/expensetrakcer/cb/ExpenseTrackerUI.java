package com.expensetrakcer.cb;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.form.AbstractForm.DeleteHandler;
import org.vaadin.viritin.form.AbstractForm.ResetHandler;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

@SpringUI
@Theme("valo")
public class ExpenseTrackerUI extends UI {

	private final ExpenseDetailsRepository repo;

	private final ExpenseEditor editor;

	private final MTable<ExpenseDetail> grid;

	private final Button addNewBtn;

	@Autowired
	public ExpenseTrackerUI(ExpenseDetailsRepository repo, ExpenseEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new MTable<>(ExpenseDetail.class).withProperties("transactionDate", "transactionAmount", "transactionType", "merchant", "description").withHeight("300px");
		this.addNewBtn = new Button("New expense", FontAwesome.PLUS_CIRCLE);
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
				editor.setEntity(new ExpenseDetail("", new Date(), new Double("0"), "", ""));
			}
		});

		// Listen changes made by the editor, refresh data from backend
		editor.setSavedHandler(new SavedHandler<ExpenseDetail>() {
			@Override
			public void onSave(ExpenseDetail ed) {
				repo.save(ed);
				listExpenses();
				editor.setVisible(false);
			}
		});

		editor.setResetHandler(new ResetHandler<ExpenseDetail>() {
			@Override
			public void onReset(ExpenseDetail ed) {
				editor.setVisible(false);
				listExpenses();
			}
		});

		editor.setDeleteHandler(new DeleteHandler<ExpenseDetail>() {
			@Override
			public void onDelete(ExpenseDetail ed) {
				repo.delete(ed);
				listExpenses();
			}
		});

		// Initialize listing
		listExpenses();

		// build layout
		setContent(new MVerticalLayout(addNewBtn, grid, editor));
	}

	private void listExpenses() {
        grid.setBeans(repo.findAll());
	}
}
