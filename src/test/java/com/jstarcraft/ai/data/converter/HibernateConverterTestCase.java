package com.jstarcraft.ai.data.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.DataSpace;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HibernateConverterTestCase {

	private final static String selectDataHql = "select data.leftUser, data.rightUser, data.leftItem, data.rightItem, data.score from MockData data";

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void testConvert() throws Exception {
		Map<String, Class<?>> qualityDifinitions = new HashMap<>();
		Map<String, Class<?>> continuousDifinitions = new HashMap<>();
		qualityDifinitions.put("user", int.class);
		qualityDifinitions.put("item", int.class);
		continuousDifinitions.put("score", float.class);
		DataSpace space = new DataSpace(qualityDifinitions, continuousDifinitions);

		TreeMap<Integer, String> configuration = new TreeMap<>();
		configuration.put(2, "user");
		configuration.put(4, "item");
		configuration.put(5, "score");

		HibernateConverter converter = new HibernateConverter(space.getQualityAttributes(), space.getContinuousAttributes());
		{
			DataModule dense = space.makeDenseModule("dense", configuration, 1000);
			List<MockData> datas = new ArrayList<>(5);
			for (int index = 1; index <= 5; index++) {
				MockData data = new MockData(index, 1, 2, 3, 4, index / 10F);
				datas.add(data);
			}
			Session session = sessionFactory.openSession();
			{
				Transaction transaction = session.beginTransaction();
				for (MockData data : datas) {
					session.save(data);
				}
				transaction.commit();
			}
			Query query = session.createQuery(selectDataHql);
			ScrollableResults iterator = query.scroll();
			int count = converter.convert(dense, iterator);
			Assert.assertEquals(5, count);
			iterator.close();
			Transaction transaction = session.beginTransaction();
			for (MockData data : datas) {
				session.delete(data);
			}
			transaction.commit();
			session.close();
		}
		{
			DataModule sparse = space.makeSparseModule("sparse", configuration, 1000);
			List<MockData> datas = new ArrayList<>(5);
			datas.add(new MockData(1, 1, null, null, null, null));
			datas.add(new MockData(2, 1, 2, null, null, null));
			datas.add(new MockData(3, 1, 2, 3, null, null));
			datas.add(new MockData(4, 1, 2, 3, 4, null));
			datas.add(new MockData(5, 1, 2, 3, 4, 0.5F));
			Session session = sessionFactory.openSession();
			{
				Transaction transaction = session.beginTransaction();
				for (MockData data : datas) {
					session.save(data);
				}
				transaction.commit();
			}
			Query query = session.createQuery(selectDataHql);
			ScrollableResults iterator = query.scroll();
			int count = converter.convert(sparse, iterator);
			Assert.assertEquals(5, count);
			iterator.close();
			Transaction transaction = session.beginTransaction();
			for (MockData data : datas) {
				session.delete(data);
			}
			transaction.commit();
			session.close();
		}
	}

}
