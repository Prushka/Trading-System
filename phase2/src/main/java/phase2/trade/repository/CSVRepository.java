package phase2.trade.repository;

import com.opencsv.bean.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

/**
 * The CSV implementation of storing and reading the list of entities to and from CSV files.
 *
 * @param <T> The entity this Repository deals with
 * @author Dan Lyu
 * @author lecture code, Logging project
 * @see RepositorySaveImpl
 */
public class CSVRepository<T extends UniqueId> extends RepositorySaveImpl<T> {

    private final Class<T> clazz;

    public CSVRepository(Class<T> clazz, String path, SaveHook saveHook) {
        super(path, saveHook);
        data = new ArrayList<>();
        this.clazz = clazz;
        if (file.exists()) {
            readUnsafe();
        }
    }

    private void readUnsafe() {
        try {
            Reader reader = Files.newBufferedReader(file.toPath());

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<T> iterator = csvToBean.iterator();

            data = new ArrayList<>();
            while (iterator.hasNext()) {
                data.add(iterator.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUnsafe() {
        try {
            // Creating writer class to generate
            // csv file
            FileWriter writer = new
                    FileWriter(file);

            // Create Mapping Strategy to arrange the
            // column name in order
            ColumnPositionMappingStrategy<T> mappingStrategy =
                    new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(clazz);

            // Arrange column name as provided in below array.
            String[] columns = new String[]
                    {"Name", "Age", "Company", "Salary"};
            mappingStrategy.setColumnMapping(columns);

            StatefulBeanToCsvBuilder<T> builder =
                    new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<T> beanWriter =
                    builder.withMappingStrategy(mappingStrategy).build();

            beanWriter.write(data);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* https://www.callicoder.com/java-read-write-csv-file-opencsv/
      public static void main(String[] args) throws IOException,
            CsvDataTypeMismatchException,
            CsvRequiredFieldEmptyException {

        try (
            Writer writer = Files.newBufferedWriter(Paths.get(STRING_ARRAY_SAMPLE));
        ) {
            StatefulBeanToCsv<MyUser> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            List<MyUser> myUsers = new ArrayList<>();
            myUsers.add(new MyUser("Sundar Pichai ♥", "sundar.pichai@gmail.com", "+1-1111111111", "India"));
            myUsers.add(new MyUser("Satya Nadella", "satya.nadella@outlook.com", "+1-1111111112", "India"));

            beanToCsv.write(myUsers);
        }
    }
     */

    @Override
    public void save() {
        saveUnsafe();
    }
}
