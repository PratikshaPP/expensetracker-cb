/**
 * 
 */
package com.expensetrakcer.cb;

import java.util.Date;
import java.util.List;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;

/**
 * @author eratnch
 *
 */
@ViewIndexed(designDoc = "expenseDetail")
@N1qlPrimaryIndexed
public interface ExpenseDetailsRepository extends CouchbasePagingAndSortingRepository<ExpenseDetail, String> {
	
	List<ExpenseDetail> findAll();
	
	List<ExpenseDetail> findByTransactionDate(Date transactionDate);
	
	List<ExpenseDetail> findByMerchant(String merchantName);

}