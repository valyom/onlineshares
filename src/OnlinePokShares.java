
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import online.pok.defines.GlobalDefines;
import online.pok.impl.PokReaderSharePricesFactory;

public class OnlinePokShares {
    private static String what = "all";
    private static LocalDate firstDate = LocalDate.now();
    private static LocalDate endDate = null;
    String[] whatsAllowed = { "all", "dpf", "ppf", "upf", "bnb", "usd", "xau" };

    private static void printHelp() {
        System.out.println("\nPrints the share prices for a day or for a period.\n");
        System.out.println("Command Lie :\t [-w dpf|upf|ppf|all|usd|xau] [-d date] [-d1 date] [-h]");

        System.out.println("-w \t What to print. Default to 'all'. Also possible 'dpf', 'upf', 'ppf'");
        System.out.println("   \t all will not include usd cources.");
        System.out.println("   \t If -d is ommitted today is reported if there are data available.");
        System.out.println("   \t Else the first closest day with data before today this is reported.\n");

        System.out.println("-d \t First date of the period or the exact date if no -d1 privided.");
        System.out.println("   \t Default today.\n");

        System.out.println("-d1\t End date of the period. Default or error - today\n");

        System.out.println("   \t d is always the first, d1 is always the last. If d > d1 the");
        System.out.println("   \t dates will be reported from d to d1 nevertheless\n");

        System.out.println("-h \t Prints this help.");
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
            // in case of 03.04.2024  => will assume  Third of Aprinl (3rd April)
            //in case of 03/04/2024  => will assume   4th   of March  (March, 4th)
            boolean bgFormat = dateStr.indexOf(".") >=0;    


            String[] dateParts = dateStr.split("[./]");
            if (dateParts.length == 3) {
                int y = strToInt(dateParts[2]);
                int d = bgFormat ?  strToInt(dateParts[0]) : strToInt(dateParts[1]);
                int m = bgFormat ?  strToInt(dateParts[1]) : strToInt(dateParts[0]);

                return   LocalDate.parse(String.format("%04d-%02d-%02d",y,m,d), GlobalDefines.yyyyMMdd);
            }
        } catch (DateTimeParseException e) {
        }
        return firstDate;
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
                    firstDate = parseDateStr(args[k + 1]);
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

        if (firstDate.isAfter(LocalDate.now()) || firstDate.isBefore(LocalDate.of(2010, 1, 10))) {
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

    public void run(String[] args) {
        if (!this.parseCommandLine(args)) {
            return;
        }

        if (endDate != null && !firstDate.isEqual(endDate)) {
            PokReaderSharePricesFactory.create(what).sharePricesPeriod(firstDate, endDate);
        } else {
            PokReaderSharePricesFactory.create(what).sharePricesValidAtDate(firstDate);
        }
    }

    public static void main(String[] args) {
        String[] params = { "-w", "xau"};// "xau" , "-d", "04.09.2024" };
       
        (new OnlinePokShares()).run(args.length > 0 ? args : params);
    }
}

