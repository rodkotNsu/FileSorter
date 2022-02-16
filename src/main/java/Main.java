import java.util.Arrays;

/**
 * {@link Main} it`s class configuration working {@link Parser} by value of command line switches
 * @implNote
 *<p>The program parameters are set at startup via command line arguments, in order:</p>
 *<p>1. sorting mode (-a or -d), optional, by default sort in ascending order;</p>
 *<p>2. data type (-s or -i), required;</p>
 *<p>3. output file name, required;</p>
 *<p>4. other parameters - names of input files, at least one.</p>
 * @see Main#main(String[])
 * @see Parser
 * @author Kotov Rodion
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Error parameters program : missing parameters");
            return;
        }
        int sortMode=Parser.ASCEND_SORT_MODE;
        switch (args[0]) {
            case "-a" -> {}
            case "-d" -> sortMode = Parser.DESCENT_SORT_MODE;
            default -> {
                System.err.println("Error parameters program:" +
                        "\""+args[0]+"\" it`s non-existent parameter");
                return;
            }
        }
        switch (args[1]) {
            case "-i" -> {
                Parser<Integer> parser = new Parser<>(sortMode,
                        Arrays.stream(args).toList().subList(3, args.length),
                        args[2],
                        Parser.functionTransLineToInteger);
                parser.sortFiles();
            }
            case "-s" -> {
                Parser<String> parser = new Parser<>(sortMode,
                        Arrays.stream(args).toList().subList(3, args.length),
                        args[2],
                        Parser.functionTransLineToString);
                parser.sortFiles();
            }

            default -> {
                System.err.println("Error parameters program:" +
                        "\""+args[0]+"\" it`s non-existent parameter");
            }
        }
    }
}
