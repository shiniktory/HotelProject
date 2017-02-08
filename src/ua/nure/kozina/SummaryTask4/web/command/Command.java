package ua.nure.kozina.SummaryTask4.web.command;

import ua.nure.kozina.SummaryTask4.exception.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * The Command class provides a default behavior like performing client's requests.
 *
 * @author V. Kozina-Kravchenko
 */
public abstract class Command implements Serializable {

    /**
     * The flag variable that shows is after current command need to be redirection or forward
     * to another page. If true - redirection. By default forward.
     */
    private boolean isRedirect = false;

    /**
     * Constructs a new Command instance.
     */
    public Command() {
    }

    /**
     * Constructs a new Command instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public Command(boolean isRedirect) {
        this.isRedirect = isRedirect;
    }

    public void setRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
    }

    /**
     * Returns a current flag state.
     *
     * @return a current flag state
     */
    public boolean isRedirect() {
        return isRedirect;
    }

    /**
     * Performs a data processing and returns a path to the next page or command.
     *
     * @param request  an information about HTTP request
     * @param response an information about HTTP response
     * @return a path to the next page or command
     * @throws ServletException
     * @throws IOException
     * @throws ApplicationException
     */
    public abstract String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException;

}
