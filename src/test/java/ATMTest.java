import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ATMTest {
    /*
    @Mock
    private Bank mockBank = new Bank();
    @InjectMocks
    private ATM mockATM;
    private User mockUser;
    */
    private Bank mockBank;
    private ATM mockATM;
    private User mockUser;

@BeforeEach
    public void initTests() {
    //MockitoAnnotations.openMocks(this);
    mockBank = mock(Bank.class);
    mockATM = new ATM(mockBank);
    mockUser = new User("01", "1234", 1234);
    //mockATM = new ATM(mockBank);
    //mockUser = new User("01", "1234", 1234);

    //mockBank = mock(Bank.class);
    //mockATM = new ATM(mockBank);
    //mockBank.addUser(mockUser);
    //when(mockBank.addUser(mockUser).thenAnswer(invocation -> invocation.getArgument(0));
    //TO-DO: current session for mocking purposes
}
    @Test
    @DisplayName("Verify bank name.")
    public void getBankNameFromStaticMethod() {
        try (MockedStatic<Bank> bankMockedStatic = mockStatic(Bank.class)) {
            bankMockedStatic.when(Bank::getBankName).thenReturn("MockBank");
            String bankName = Bank.getBankName();
            assertEquals("MockBank", bankName);
            bankMockedStatic.verify(Bank::getBankName, times(1));
        }
    }
@Test
@DisplayName("Card has been removed.")
public void card_hasBeenRemoved() {
    assertFalse(mockATM.checkCard());
}
@Test
@DisplayName("Card is still in ATM")
public void card_isStillIn() {
    when(mockATM.insertCard(mockUser)).thenReturn(true);
    when(mockBank.isCardLocked("01")).thenReturn(false);
    mockATM.insertCard(mockUser);
    assertTrue(mockATM.checkCard());
}

@Test
@DisplayName("Card not inserted or recognized.")
public void card_NotInserted() {
    mockATM.insertCard(null);
    assertFalse(mockATM.insertCard(null));
}
@Test
    @DisplayName("Card inserted and isn't locked.")
    public void cardInserted_CardNotLocked() {
        mockATM.insertCard(mockUser);
        assertTrue(mockATM.insertCard(mockUser));
    }
    @Test
    @DisplayName("Card inserted but is locked.")
    public void cardInserted_CardIsLocked() {
    when(mockBank.isCardLocked("01")).thenReturn(true);
    mockATM.insertCard(mockUser);
    assertFalse(mockATM.insertCard(mockUser));
    }

    @Test
    @DisplayName("Look for users in Bank")
    public void lookingUpUser_UserIsNotFound(){
    assertNull(mockBank.getUserById(mockUser.getId()));
    }
    @Test
    @DisplayName("Pin entered successfully.")
    public void pinCodeEntered_Correct() {
    mockATM.insertCard(mockUser);
        //TO-DO create a mockUser in mockBank
    when(mockBank.pinValidate("01", "1234")).thenReturn(true);
    assertTrue(mockATM.enterPin("1234"));
    }

@Test
    @DisplayName("Wrong pin entered.")
    public void pinCodeEntered_Incorrect() {
    mockATM.insertCard(mockUser);
    assertFalse(mockATM.enterPin("1111"));
    }

@Test
    @DisplayName("Test incorrect pin validation")
    public void pinCode_ValidationFails() {
    assertFalse(mockBank.pinValidate("01", "3333"));
    }
    @Test
    @DisplayName("Test correct pin validation")
    public void pinCode_ValidationSucceeds() {
        //TO-DO create a mockUser in mockBank
        when(mockBank.pinValidate("01", "1234")).thenReturn(true);
        assertTrue(mockBank.pinValidate("01", "1234"));
    }

@ParameterizedTest
@ValueSource(strings = {"0000", "1234"})
    @DisplayName("User enters wrong pin once, then succeeds on second try.")
    public void pinCodeEntered_SecondTry(String sourceInputs) {
    //when(mockATM.checkCard()).thenReturn(true);
    mockATM.insertCard(mockUser);
    assertFalse(mockATM.enterPin(sourceInputs));
}
    @ParameterizedTest
    @ValueSource(strings = {"1111", "2222", "3333"})
    @DisplayName("User enters wrong pin three times, card gets locked.")
    public void pinCodeEntered_CardGetsLocked(String sourceInputs) {
        //TO-DO don't reset the user each time.
        mockATM.insertCard(mockUser);
        when(mockBank.pinValidate(mockUser.getId(), sourceInputs)).thenReturn(false);
        assertFalse(mockATM.enterPin(sourceInputs));
    }
    @Test
    @DisplayName("Check the current users' current balance")
    public void currentBalance_ReturnsCorrectAmount() {
        //TO-DO don't reset the user each time.
        mockATM.insertCard(mockUser);
        assertEquals(1234, mockATM.checkBalance(), "Expecting 1234.");
    }
    @Test
    @DisplayName("Tries to withdraw too much money from account")
    public void withdraw_NotEnoughFunds() {
        mockATM.insertCard(mockUser);
        assertFalse(mockATM.withdraw(5000));
    }

    @Test
    @DisplayName ("Successful withdrawal")
    public void withdraw_Successful_CorrectAmountLeft() {
        mockATM.insertCard(mockUser);
        mockATM.withdraw(234);
        assertEquals(1000, mockUser.getBalance());
    }
    @Test
    @DisplayName ("Successful deposit")
    public void deposit_Successful_CorrectAmountNew() {
        mockATM.insertCard(mockUser);
        mockATM.deposit(1000);
        assertEquals(2234, mockUser.getBalance());
    }

    @Test
    @DisplayName("Remove card")
    public void cardIsRemoved_clearCurrentUser() {
        mockATM.removeCard();
        assertFalse(mockATM.checkCard());
    }
}