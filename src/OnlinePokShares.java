
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import online.pok.defines.GlobalDefines;
import online.pok.impl.PokReaderSharePricesFactory;

public class OnlinePokShares {
    private static String what = "all";
    private static LocalDate today = LocalDate.now();
    private static LocalDate endDate = null;
    String[] whatsAllowed = { "all", "dpf", "ppf", "upf", "usd" };

    private static void printHelp() {
        System.out.println("\nPrints the share prices for a day or for a period.\n");
        System.out.println("Command Lie :\t [-w dpf|upf|ppf|all] [-d date] [-h]");
        System.out.println("-w :\t What fond to print. default to 'all'. Also possible 'dpf', 'upf', 'ppf'");
        System.out.println("    \t If -d is ommitted today is reported if there are data available.");
        System.out.println("    \t Else the first closest day with data before today this is reported.");
        System.out.println("-d :\t For which date to report or start of the period if -d1.");
        System.out.println("    \t If it is a holyday, the first working date before this is reported.");
        System.out.println("    \t In case what is not 'all', this gives the first date of a period ending");
        System.out.println("    \t with today, which will be reported one day per line. Default - today");
        System.out.println("-d1 :\t End date of the period to report . Default or error - today");

        System.out.println("-h :\t Prints this help.");
    };

    private int strToInt(String input) {
        // if (input.matches("\\d+")) {}
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private LocalDate parseDateStr(String dateStr) {
        // String date = args[k + 1].replaceAll("\\.", "-").replaceAll("\\/", "-");
        try {
            String[] dateParts = dateStr.split("[-./]");
            if (dateParts.length == 3) {
                return LocalDate.parse(String.format("%04d-%02d-%02d", strToInt(dateParts[2]), strToInt(dateParts[1]),
                        strToInt(dateParts[0])), GlobalDefines.yyyyMMdd);
            }
        } catch (DateTimeParseException e) {
        }
        return today;
    }

    private boolean parseCommandLine(String[] args) {
        int k = 0;
        while (args.length > k) {
            int numSubParams = 0;
            if ("-w".compareToIgnoreCase(args[k]) == 0) {
                if (args.length > k + 1) {
                    what = args[k + 1];
                    ++numSubParams;
                }
            } else if ("-d".compareToIgnoreCase(args[k]) == 0) {
                if (args.length > k + 1) {
                    today = parseDateStr(args[k + 1]);
                    ++numSubParams;
                }
            } else if ("-d1".compareToIgnoreCase(args[k]) == 0) {
                if (args.length > k + 1) {
                    endDate = parseDateStr(args[k + 1]);
                    ++numSubParams;
                }

            } else if ("-h".compareToIgnoreCase(args[k]) == 0) {
                OnlinePokShares.printHelp();
                return false;
            }

            k += 1 + numSubParams;
        }

        if (today.isAfter(LocalDate.now()) || today.isBefore(LocalDate.of(2010, 1, 10))) {
            return false;
        }

        boolean whatParamOk = false;
        for (String w : whatsAllowed) {
            if (w.compareToIgnoreCase(what) == 0) {
                whatParamOk = true;
                break;
            }
        }

        return whatParamOk;
    }

    private void run(String[] args) {
        if (!this.parseCommandLine(args)) {
            return;
        }

        if (endDate != null && !today.isEqual(endDate)) {
            PokReaderSharePricesFactory.create(what).sharePricesPeriod(today, endDate);
        } else {
            PokReaderSharePricesFactory.create(what).sharePricesValidAtDate(today);
        }
    }

    public static void main(String[] args) {

        String[] params = { "-w", "all"}; // , "-d", "24.01.2024", "-d1", "12.01.2024"  };
        (new OnlinePokShares()).run(args.length > 0 ? args : params);

    }

}