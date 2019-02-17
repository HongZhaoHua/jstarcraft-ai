package com.jstarcraft.ai.data.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.DataSpace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JdbcConverterTestCase {

	private final static String createTableSql = "create table MockData (id integer, left_user integer, right_user integer, left_item integer, right_item integer, score float, primary key (id))";

	private final static String createDataSql = "insert into MockData (id, left_user, right_user, left_item, right_item, score) values (?, ?, ?, ?, ?, ?)";

	private final static String selectDataSql = "select left_user, right_user, left_item, right_item, score from MockData";

	private final static String deleteDataSql = "delete from MockData";

	private final static String deleteTableSql = "drop table MockData if exists";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testConvert() throws Exception {
		jdbcTemplate.update(deleteTableSql);
		jdbcTemplate.update(createTableSql);

		Map<String, Class<?>> discreteDifinitions = new HashMap<>();
		Map<String, Class<?>> continuousDifinitions = new HashMap<>();
		discreteDifinitions.put("user", int.class);
		discreteDifinitions.put("item", int.class);
		continuousDifinitions.put("score", float.class);
		DataSpace space = new DataSpace(discreteDifinitions, continuousDifinitions);

		TreeMap<Integer, String> configuration = new TreeMap<>();
		configuration.put(2, "user");
		configuration.put(4, "item");
		configuration.put(5, "score");

		JdbcConverter converter = new JdbcConverter(space.getDiscreteAttributes(), space.getContinuousAttributes());
		{
			DataModule dense = space.makeDenseModule("dense", configuration, 1000);
			for (int index = 1; index <= 5; index++) {
				jdbcTemplate.update(createDataSql, index, 1, 2, 3, 4, index / 10F);
			}
			jdbcTemplate.query(selectDataSql, (iterator) -> {
				int count = converter.convert(dense, iterator);
				Assert.assertEquals(5, count);
				return null;
			});
			jdbcTemplate.update(deleteDataSql);
		}
		{
			DataModule sparse = space.makeSparseModule("sparse", configuration, 1000);
			jdbcTemplate.update(createDataSql, 1, 1, null, null, null, null);
			jdbcTemplate.update(createDataSql, 2, 1, 2, null, null, null);
			jdbcTemplate.update(createDataSql, 3, 1, 2, 3, null, null);
			jdbcTemplate.update(createDataSql, 4, 1, 2, 3, 4, null);
			jdbcTemplate.update(createDataSql, 5, 1, 2, 3, 4, 0.5F);
			jdbcTemplate.query(selectDataSql, (iterator) -> {
				int count = converter.convert(sparse, iterator);
				Assert.assertEquals(5, count);
				return null;
			});
			jdbcTemplate.update(deleteDataSql);
		}
	}

}
