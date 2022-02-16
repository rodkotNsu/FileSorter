import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Class {@link Parser} implements the sort interface by merging files {@code nameFiles} into an output file {@code outFile}
 * @param <TypeValue> it`s type values in files.
 * An interface {@link Comparable} must be implemented in a type  {@param TypeValue} class.
 * <p>
 *   Param {@code functionTrans} it`s lambda function,which converts a {@link String} to a value of type TypeValue.
 *   Param {@code sortMode} it`s mode of sorting files. </p>
 *
 *@implNote
 * <p>In case of an error opening/reading input files from the list,
 * the program continues to work with other files, deleting the unnecessary file. </p>
 *
 * <p>If the file contains incorrect values,
 * the program displays a message about the content of incorrect data and continues to work with the file.</p>
 *
 * <p>Errors in the initial sorting in the input file, the program looks for the first element in the damaged file,
 * which restores the order and continues to work with the found value</p>
 *
 * @see Parser#sortFiles()
 *
 * @author Kotov Rodion
 */
public class Parser<TypeValue extends Comparable<TypeValue>> {
    public static final int ASCEND_SORT_MODE = 1;
    public static final int DESCENT_SORT_MODE = -1;

    private int sortMode = ASCEND_SORT_MODE;
    private final List<String> nameFiles;
    private final String outFile;
    private final Function<String, TypeValue> functionTrans;

    public Parser(int sortMode, List<String> nameFiles, String nameOutFile,Function<String, TypeValue> function) {
        this.outFile= nameOutFile;
        this.sortMode = sortMode;
        this.nameFiles = nameFiles;
        this.functionTrans = function;
    }
    public Parser(List<String> nameFiles, String nameOutFile,Function<String, TypeValue> function) {
        this.outFile= nameOutFile;
        this.nameFiles = nameFiles;
        this.functionTrans = function;
    }

    static public Function<String, Integer> functionTransLineToInteger = s -> {
        if (Pattern.matches("\\p{Digit}*", s))
            return Integer.parseInt(s);
        else
            return null;
    };
    static public Function<String, String> functionTransLineToString = s -> {
        if (s.contains(" "))
            return null;
        else
            return s;
    };

    /**
     * @return a list of already opened input files
     */
    private List<Pair<BufferedReader, TypeValue>> getListPairBuffStrOut() {
        List<Pair<BufferedReader, TypeValue>> fileSteam = new ArrayList<>(nameFiles.size());
        for (String nameFile : nameFiles) {
            FileReader fileReader;
            try {
                fileReader = new FileReader(nameFile);
                fileSteam.add(new Pair<>(new BufferedReader(fileReader), null));
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
        return fileSteam;
    }

    private void closeFile(Pair<BufferedReader, TypeValue> bufferedReader) {
        try {
            bufferedReader.getFirst().close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void closeFile(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * it`s main method, which sorts the list of files.
     */
    public void sortFiles() {
        BufferedWriter fileOutStream;

        try {
            fileOutStream = new BufferedWriter(new FileWriter(outFile));
        } catch (IOException e) {
            System.err.println("Can`t open file \"" + outFile + "\"");
            return;
        }

        List<Pair<BufferedReader, TypeValue>> fileSteam = getListPairBuffStrOut();

        var iterator = fileSteam.iterator();
        while (iterator.hasNext()) {
            var pairFile = iterator.next();
            if (nextValue(pairFile)) {
                closeFile(pairFile);
                iterator.remove();
            }
        }

        while (!fileSteam.isEmpty()) {
            var choose = fileSteam.get(0);
            for (var pairFile : fileSteam) {
                if (sortMode * choose.getSecond().compareTo(pairFile.getSecond()) > 0) {
                    choose = pairFile;
                }
            }

            try {
                fileOutStream.write(choose.getSecond() + "\n");
            } catch (IOException e) {
                fileSteam.forEach(this::closeFile);
                return;
            }
            if (nextValue(choose)) {
                closeFile(choose);
                fileSteam.remove(choose);
            }
        }
        closeFile(fileOutStream);
    }

    /**
     * Gets a new value
     * @param pair is {@link Pair}, which contains the open file and the current symbol.
     * @return true- new value is null.
     *         false- new value isn`t null.
     */
    private boolean nextValue(Pair<BufferedReader, TypeValue> pair) {
        while (true) {
            String line;
            try {
                line = pair.getFirst().readLine();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                pair.setSecond(null);
                return true;
            }
            if (line == null) {
                pair.setSecond(null);
                return true;
            } else {
                TypeValue value = functionTrans.apply(line);
                if (value != null) {
                    if (pair.secondIsNull() || sortMode * value.compareTo(pair.getSecond()) > 0) {
                        pair.setSecond(value);
                        return false;
                    }
                } else
                    System.err.println("Error Input Format \"" + line + "\"");
            }
        }
    }
}
