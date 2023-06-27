import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@WebServlet(value = "/time")
public class TimeServlet  extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("<html><body>");
        out.print("<h3>Hello! This is Time Servlet!</h3>");
        response.setHeader("Refresh", "1");

        String timeZoneParam = request.getParameter("timezone");
        ZoneId zoneId = parseTimeZone(timeZoneParam).orElse(ZoneId.of("UTC+3"));

        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        ZoneOffset currentOffset = currentTime.getOffset();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

        out.write("<p>" + formattedTime + " (" + formatOffset(currentOffset) + ")</p>");
        out.print("<h4>Current Timezone: " + zoneId + "</h4>");

        out.print("</body></html>");
        out.close();
    }

    private Optional<ZoneId> parseTimeZone(String timeZoneParam) {
        if (timeZoneParam == null || timeZoneParam.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(ZoneId.of(timeZoneParam));
        } catch (Exception e) {
            try {
                int offsetHours = Integer.parseInt(timeZoneParam.substring(4));
                return Optional.of(ZoneOffset.ofHours(offsetHours).normalized());
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
    }

    private String formatOffset(ZoneOffset offset) {
        int totalSeconds = offset.getTotalSeconds();
        int hours = totalSeconds / 3600;
        int minutes = Math.abs(totalSeconds % 3600) / 60;

        return String.format("%+03d:%02d", hours, minutes);
    }
}

