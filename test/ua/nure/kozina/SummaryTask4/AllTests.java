package ua.nure.kozina.SummaryTask4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ua.nure.kozina.SummaryTask4.DB.*;
import ua.nure.kozina.SummaryTask4.stateAndRole.TestStatesAndRole;
import ua.nure.kozina.SummaryTask4.util.TestMailSender;
import ua.nure.kozina.SummaryTask4.util.TestMessageUtil;
import ua.nure.kozina.SummaryTask4.util.TestSorterUtil;
import ua.nure.kozina.SummaryTask4.validator.TestDataValidator;
import ua.nure.kozina.SummaryTask4.web.TestControllerServlet;
import ua.nure.kozina.SummaryTask4.web.command.TestClientCommands;
import ua.nure.kozina.SummaryTask4.web.command.TestCommandFactory;
import ua.nure.kozina.SummaryTask4.web.command.TestAdminCommands;
import ua.nure.kozina.SummaryTask4.web.command.TestCommonCommands;
import ua.nure.kozina.SummaryTask4.web.filter.TestEncodingFilter;
import ua.nure.kozina.SummaryTask4.web.tag.TestUserInfoTag;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestDBManager.class,
        TestStatusInspector.class,
        TestApartmentManager.class,
        TestOrderManager.class,
        TestRoomRequestManager.class,
        TestUserManager.class,
        TestStatesAndRole.class,
        TestMessageUtil.class,
        TestMailSender.class,
        TestSorterUtil.class,
        TestDataValidator.class,
        TestControllerServlet.class,
        TestCommandFactory.class,
        TestAdminCommands.class,
        TestClientCommands.class,
        TestCommonCommands.class,
        TestUserInfoTag.class,
        TestEncodingFilter.class
})
public class AllTests {
}
