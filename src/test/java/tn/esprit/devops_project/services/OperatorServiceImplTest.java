package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
import tn.esprit.devops_project.entities.Operator;

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
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveAllOperators() {
        final List<Operator> allOperators = this.operatorService.retrieveAllOperators();
        assertEquals(allOperators.size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void addOperator() {
        final Operator operator2 = new Operator();
        operator2.setFname("samir");
        operator2.setLname("samm");
        operator2.setPassword("123456");
        this.operatorService.addOperator(operator2);

        assertEquals(this.operatorService.retrieveAllOperators().size(), 2);
        assertEquals(this.operatorService.retrieveOperator(2L).getFname(), "samir");

    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void deleteOperator() {
        // Get the initial number of operators
        int initialOperatorCount = this.operatorService.retrieveAllOperators().size();

        // Delete an operator (assuming you have a delete method in your service)
        this.operatorService.deleteOperator(1L);

        // Verify that the number of operators has decreased by one
        int updatedOperatorCount = this.operatorService.retrieveAllOperators().size();
        assertEquals(initialOperatorCount - 1, updatedOperatorCount);
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void updateOperator() {
        // Get an operator from the database
        Operator operator = this.operatorService.retrieveOperator(1L);

        // Update the operator's first name
        operator.setFname("Alaa");

        // Call the update method in your service
        this.operatorService.updateOperator(operator);

        // Retrieve the updated operator
        Operator updatedOperator = this.operatorService.retrieveOperator(1L);

        // Verify that the first name has been updated
        assertEquals("Alaa", updatedOperator.getFname());
    }


    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveOperator() {
        final Operator operator = this.operatorService.retrieveOperator(1L);
        assertEquals("Damm", operator.getLname());
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveOperator_nullId() {
        assertThrows(NullPointerException.class, () -> {
            final Operator operator = this.operatorService.retrieveOperator(100L);

        });
    }

}