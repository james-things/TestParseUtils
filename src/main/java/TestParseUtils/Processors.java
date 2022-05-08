package TestParseUtils;

import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * A class which defines CSV processors for each analyzed project
 **/
public class Processors {
    CellProcessor[] processor;

    public Processors(String targetFile) {
        if (targetFile == "j") processor = getjProcessors();
        if (targetFile == "tm") processor = gettmProcessors();
        if (targetFile == "sl") processor = getslProcessors();
        if (targetFile == "jj") processor = getjjProcessors();
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

    public CellProcessor[] getjjProcessors() {
        return new CellProcessor[] { // "suite", "test", "totalCov", "cdl", "cookie", "cookieList", "http", "httpTokener", "jsonArray", "jsonException", "jsonMl", "jsonObject", "jsonPointer", "jsonPointerException", "jsonStringer", "jsonTokener", "jsonWriter", "property", "xml", "xmlParserConfiguration", "xmlTokener", "time", "status"
                new NotNull(), // Suite
                new NotNull(), // Test
                new NotNull(new ParseInt()), // totalCov
                new NotNull(new ParseInt()), // cdl
                new NotNull(new ParseInt()), // cookie
                new NotNull(new ParseInt()), // cookieList
                new NotNull(new ParseInt()), // http
                new NotNull(new ParseInt()), // httpTokener
                new NotNull(new ParseInt()), // jsonArray
                new NotNull(new ParseInt()), // jsonException
                new NotNull(new ParseInt()), // jsonMl
                new NotNull(new ParseInt()), // jsonObject
                new NotNull(new ParseInt()), // jsonPointer
                new NotNull(new ParseInt()), // jsonPointerException
                new NotNull(new ParseInt()), // jsonStringer
                new NotNull(new ParseInt()), // jsonTokener
                new NotNull(new ParseInt()), // jsonWriter
                new NotNull(new ParseInt()), // property
                new NotNull(new ParseInt()), // xml
                new NotNull(new ParseInt()), // xmlParserConfiguration
                new NotNull(new ParseInt()), // xmlTokener
                new NotNull(new ParseInt()), // time
                new NotNull(), // status
        };
    }
}
