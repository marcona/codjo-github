package net.codjo.pyp.services;
import net.codjo.pyp.model.Brin;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 *
 */
public class CsvService {
    private static final String SEPARATOR = ";";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TEXT_SEPARATOR = "\"";


    private CsvService() {
    }


    public static StringBuilder export(List<Brin> brinList) {
        StringBuilder builder = new StringBuilder();

        String[] declaredFields = new String[]{"title", "creationDate", "status", "description", "affectedTeams",
                                               "unblockingType", "unblockingDate", "unblockingDescription",
                                               "rootCause"};
        for (String declaredField : declaredFields) {
            builder.append(declaredField).append(SEPARATOR);
        }
        builder.append("\n");

        for (Brin brin : brinList) {
            exportBrin(builder, brin);
        }
        return builder;
    }


    private static void exportBrin(StringBuilder builder, Brin brin) {

        builder.append(brin.getTitle())
              .append(SEPARATOR)
              .append(formatEmptyDate(brin.getCreationDate()))
              .append(SEPARATOR)
              .append(brin.getStatus())
              .append(SEPARATOR)
              .append(TEXT_SEPARATOR).append(formatEmpty(brin.getDescription())).append(TEXT_SEPARATOR)
              .append(SEPARATOR)
              .append(brin.getAffectedTeams().isEmpty() ? "" : brin.getAffectedTeams())
              .append(SEPARATOR)
              .append(formatEmpty(brin.getunblockingType()))
              .append(SEPARATOR)
              .append(formatEmptyDate(brin.getUnBlockingDate()))
              .append(SEPARATOR)
              .append(TEXT_SEPARATOR).append(formatEmpty(brin.getUnBlockingDescription())).append(TEXT_SEPARATOR)
              .append(SEPARATOR)
              .append(TEXT_SEPARATOR).append(formatEmpty(brin.getRootCause())).append(TEXT_SEPARATOR)
              .append(SEPARATOR)
              .append("\n");
    }


    private static Object formatEmpty(Object unblockingType) {
        return unblockingType != null ? unblockingType : "";
    }


    private static Object formatEmptyDate(Date unblockingType) {
        return unblockingType != null ? DATE_FORMAT.format(unblockingType) : "";
    }
}
