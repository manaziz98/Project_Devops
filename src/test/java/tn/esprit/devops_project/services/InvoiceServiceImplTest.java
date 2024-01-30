package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hibernate.collection.internal.PersistentSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.repositories.SupplierRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class InvoiceServiceImplTest {
    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired OperatorServiceImpl operatorService;
    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
        final List<Invoice> invoiceList = this.invoiceService.retrieveAllInvoices();
        assertEquals(invoiceList.size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        invoiceService.cancelInvoice(1L);
        final Invoice invoice = invoiceService.retrieveInvoice(1L);
        assertEquals(true, invoice.getArchived());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice() {
        final Invoice invoice = this.invoiceService.retrieveInvoice(1L);
        assertEquals(false, invoice.getArchived());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void testGetTotalAmountInvoicesForSupplier() {
        Long supplierId = 1L;

        float totalAmount = invoiceService.getTotalAmountInvoicesForSupplier(supplierId);

        // Assuming the dataset has one invoice with amount_invoice=100.00
        assertEquals(100.00f, totalAmount, 100.00f); // Adjust the expected total as needed
    }






    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void assignOperatorToInvoice() {
        invoiceService.assignOperatorToInvoice(1L,1L);
        final Operator operator = operatorService.retrieveOperator(1L) ;
        assertTrue(operator != null && operator.getInvoices().stream().anyMatch(invoice -> invoice.getIdInvoice().equals(1L)));

    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2023-01-01");
        Date endDate = dateFormat.parse("2023-12-31");
        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);
        // Adjust this assertion based on the expected total amount from your dataset
        assertEquals(100.00f, totalAmount, 0.01f);
    }
    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Invoice invoice = this.invoiceService.retrieveInvoice(100L);
        });
    }
}