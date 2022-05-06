package TestParseUtils;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class Processors {
    CellProcessor[] processor;

    public Processors(String targetFile) {
        if (targetFile == "j") processor = getjProcessors();
        if (targetFile == "tm") processor = gettmProcessors();
        if (targetFile == "sl") processor = getslProcessors();
    }

    public CellProcessor[] getProcessor() {
        return this.processor;
    }

    public CellProcessor[] getjProcessors() {
        return new CellProcessor[] {
                new NotNull(), // Suite
                new NotNull(), // Case
                new NotNull(new ParseInt()), // Lines cov
                new NotNull(new ParseInt()), // io
                new NotNull(new ParseInt()), // Volume
                new NotNull(new ParseInt()), // Exception
                new NotNull(new ParseInt()), // BaseClass
                new NotNull(new ParseInt()), // rarfile
                new NotNull(new ParseInt()), // unpack
                new NotNull(new ParseInt()), // ppm
                new NotNull(new ParseInt()), // vm
                new NotNull(new ParseInt()), // decode
                new NotNull(new ParseInt()), // crypt
                new NotNull(new ParseInt()), // crc
                new NotNull(new ParseInt()), // unsigned
                new NotNull(new ParseInt()), // time
                new NotNull(), // status
        };
    }

    public CellProcessor[] getslProcessors() {
        return new CellProcessor[] {
                new NotNull(), // Suite
                new NotNull(), // Case
                new NotNull(new ParseInt()), // Lines cov
                new NotNull(new ParseInt()), // core
                new NotNull(new ParseInt()), // search
                new NotNull(new ParseInt()), // shuffle
                new NotNull(new ParseInt()), // sort
                new NotNull(new ParseInt()), // time
                new NotNull(), // status
        };
    }
// plain	containers	colors	arrayref	utils	special	core	advanced
    public CellProcessor[] gettmProcessors() {
        return new CellProcessor[] {
                new NotNull(), // Suite
                new NotNull(), // Case
                new NotNull(new ParseInt()), // Lines cov
                new NotNull(new ParseInt()), // plain
                new NotNull(new ParseInt()), // containers
                new NotNull(new ParseInt()), // colors
                new NotNull(new ParseInt()), // arrayref
                new NotNull(new ParseInt()), // utils
                new NotNull(new ParseInt()), // special
                new NotNull(new ParseInt()), // core
                new NotNull(new ParseInt()), // advanced
                new NotNull(new ParseInt()), // time
                new NotNull(), // status
        };
    }
}
