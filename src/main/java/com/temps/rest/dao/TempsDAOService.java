package com.temps.rest.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.temps.rest.model.Celsius;
import com.temps.rest.model.CelsiusAll;
import com.temps.rest.model.CelsiusEntity;

@Transactional
@Repository
@Component("tempsDAOService")
public class TempsDAOService implements TempsDAO {

	@Autowired
	DataSource dataSource;

	@PersistenceContext
	private EntityManager em;

	public TempsDAOService() {
		super();
	}

	@Override
	@Transactional
	public CelsiusAll create(CelsiusAll celPost) {
		Date dt = new Date(System.currentTimeMillis());
		celPost.setCreate_date(Celsius.DateFormatter.format(dt));
		celPost.setUpdate_date(Celsius.DateFormatter.format(dt));

		em.persist(celPost.objCelsiusEntity());
		em.flush();

		return celPost;
	}

	/**
	 * The iterator used here is an implementation for iterating through ResultSet rows.
	 * This approach is more scalable, reducing memory usage by not consuming memory
	 * with a collection of many elements,
	 * 
	 * (non-Javadoc)
	 * @see com.temps.rest.dao.TempsDAO#tempsAll()
	 */
	@Override
	public Iterator<CelsiusAll> tempsAll() {

		try {
			Connection dbConn = dataSource.getConnection();
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from celsius_entity");

			RS_Iterator rsIter = new RS_Iterator(rs);

			return rsIter;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	class RS_Iterator implements Iterator<CelsiusAll> {

		ResultSet resSet;
		boolean moveCursor = true;
		boolean isThereANext = true;

		public RS_Iterator(ResultSet resSet) {
			super();
			this.resSet = resSet;
		}

		@Override
		public boolean hasNext() {

			try {
				if (moveCursor) {
					isThereANext = resSet.next();
					moveCursor = false;
				}
				return isThereANext;

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public CelsiusAll next() {
			try {
				CelsiusAll ca = new CelsiusAll();
				String[] ct = resSet.getString("celsius_tempscale").split("_");
				ca.setId(resSet.getInt("id"));
				ca.setCelsius(new BigDecimal(ct[0]));
				ca.setTempScale(ct[1].charAt(0));
				ca.setCreate_date(resSet.getString("create_date"));
				ca.setUpdate_date(resSet.getString("update_date"));
				moveCursor = true;
				return ca;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	@Override
	public Collection<CelsiusAll> tempsRange(Date fromDate, Date toDate) {

		Query qry = em.createNamedQuery("findTempsRange");
		qry.setParameter("fromDt", fromDate);
		qry.setParameter("toDt", toDate);

		List<CelsiusEntity> qryLst = qry.getResultList();

		AbstractCollection<CelsiusAll> abstList =
				new AbstractCollection<CelsiusAll>() {

					@Override
					public int size() {
						return qryLst.size();
					}

					@Override
					public Iterator<CelsiusAll> iterator() {
						List<CelsiusAll> caLst = new ArrayList<CelsiusAll>();
						for (CelsiusEntity ce : qryLst) {
							caLst.add(new CelsiusAll(ce));
						}
						return caLst.iterator();
					}

				};

		return abstList;
	}

	@Override
	public CelsiusAll get(Integer id) {
		CelsiusEntity ce = em.find(CelsiusEntity.class, id);
		return new CelsiusAll(ce);
	}

	@Override
	public CelsiusAll delete(Integer id) {
		CelsiusEntity ce = em.find(CelsiusEntity.class, id);
		if (ce == null) {
			return null;
		}

		em.remove(ce);

		return new CelsiusAll(ce);
	}

	@Override
	@Transactional
	public CelsiusAll update(Integer id, CelsiusAll celsius) {
		CelsiusEntity ce = em.find(CelsiusEntity.class, id);

		ce.setCelsius_tempscale(
				celsius.objCelsiusEntity().getCelsius_tempscale()
		);
		ce.setUpdate_date(new Date(System.currentTimeMillis()));
		em.merge(ce);
		em.flush();

		return new CelsiusAll(ce);
	}
}
