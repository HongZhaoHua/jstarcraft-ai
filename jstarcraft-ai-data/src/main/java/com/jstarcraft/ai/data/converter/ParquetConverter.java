package com.jstarcraft.ai.data.converter;

import java.io.IOException;
import java.util.Collection;

import org.apache.parquet.hadoop.ParquetReader;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.attribute.QualityAttribute;
import com.jstarcraft.ai.data.attribute.QuantityAttribute;
import com.jstarcraft.ai.data.exception.DataException;

/**
 * 列式转换器
 * 
 * @author Birdy
 *
 */
public abstract class ParquetConverter<T> extends AbstractConverter<ParquetReader<T>> {

    protected ParquetConverter(Collection<QualityAttribute> qualityAttributes, Collection<QuantityAttribute> quantityAttributes) {
        super(qualityAttributes, quantityAttributes);
    }

    protected abstract void parseData(DataModule module, T parquet) throws IOException;

    @Override
    public int convert(DataModule module, ParquetReader<T> iterator) {
        try {
            int count = 0;
            while (true) {
                T parquet = iterator.read();
                if (parquet == null) {
                    break;
                }
                parseData(module, parquet);
                count++;
            }
            return count;
        } catch (Exception exception) {
            // TODO 处理日志.
            throw new DataException(exception);
        }
    }

}
