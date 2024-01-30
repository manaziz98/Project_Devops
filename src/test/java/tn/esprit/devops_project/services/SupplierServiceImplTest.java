package tn.esprit.devops_project.services;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.Supplier;

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
public class SupplierServiceImplTest {
    @Autowired
    private SupplierServiceImpl supplierService ;



    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void addSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setLabel("Label1");
        this.supplierService.addSupplier(supplier);
        assertEquals(this.supplierService.retrieveAllSuppliers().size(),3);
        assertEquals(this.supplierService.retrieveSupplier(2L).getLabel(),"Label1");
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier() {
        final Supplier supplier = this.supplierService.retrieveSupplier(1L);
        assertEquals("Label", supplier.getLabel());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveAllSupplier() {
        final List<Supplier> allSuppliers = this.supplierService.retrieveAllSuppliers();
        assertEquals(allSuppliers.size(), 2);

    }


    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void updateSupplier() {
        // Create a new supplier and save it initially
        Supplier supplier1 = new Supplier();
        supplier1.setLabel("Initial Label");
        Supplier savedSupplier = supplierService.addSupplier(supplier1);

        // Retrieve the supplier by ID
        Supplier retrievedSupplier = supplierService.retrieveSupplier(savedSupplier.getIdSupplier());

        // Verify that the retrieved supplier's label matches the initial label
        assertEquals("Initial Label", retrievedSupplier.getLabel());

        // Update the label
        retrievedSupplier.setLabel("Updated Label");
        supplierService.updateSupplier(retrievedSupplier);

        // Retrieve the supplier again
        Supplier updatedSupplier = supplierService.retrieveSupplier(savedSupplier.getIdSupplier());

        // Verify that the label has been updated
        assertEquals("Updated Label", updatedSupplier.getLabel());
    }


    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void deleteSupplier() {
        // Assuming there is a supplier with ID 1 in your test data
        Long supplierIdToDelete = 2L;

        // Delete the supplier
        supplierService.deleteSupplier(supplierIdToDelete);

        // Attempt to retrieve the supplier again


        // Verify that the deletedSupplier is null, indicating that it was deleted
        assertEquals(this.supplierService.retrieveAllSuppliers().size(),1L);
    }





}