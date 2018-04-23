package com.temps.rest.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temps.rest.dao.TempsDAO;
import com.temps.rest.model.Celsius;
import com.temps.rest.model.Fahrenheit;
import com.temps.rest.model.Temp;
import com.temps.rest.model.TempScale;

@RestController
public class TempsRestController {

	public TempsRestController() {
		super();
	}

	private final class StreamingCollection implements Collection<Temp> {
		private final Iterator<Temp> itr;

		private StreamingCollection(Iterator<Temp> itr) {
			this.itr = itr;
		}

		@Override
		public int size() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isEmpty() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean contains(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<Temp> iterator() {
			return itr;
		}

		@Override
		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean add(Temp e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends Temp> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}
	}

	private final class FahrenheitIterator implements Iterator<Temp> {
		private Iterator<Temp> itr;

		public FahrenheitIterator(Iterator<Temp> itr) {
			super();
			this.itr = itr;
		}

		@Override
		public boolean hasNext() {
			if (fahrNext)
				return true;

			return itr.hasNext();
		}

		Fahrenheit fahrObj;
		boolean fahrNext = false;

		@Override
		public Temp next() {
			if (fahrObj != null && fahrNext) {
				fahrNext = false;
				return fahrObj;
			}
			Temp t = itr.next();
			if (t instanceof Fahrenheit) {
				fahrObj = (Fahrenheit) t;
				fahrNext = true;
				return fahrObj.objCelsius();
			} else {
				return t;
			}

		}

	}

	/**
	 * Wired with Testing DAO
	 */
	@Autowired
	@Qualifier("tempsDAOProto")
	private TempsDAO tempsDAO;

	@GetMapping(path = "/temps", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Temp>> getTemps() {

		FahrenheitIterator fItr = new FahrenheitIterator(tempsDAO.temps());

		Collection<Temp> c = new StreamingCollection(fItr);

		return new ResponseEntity<Collection<Temp>>(c, HttpStatus.OK);

	}

	@GetMapping(path = "/tempsStream", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Temp>> getTempsStream() {

		FahrenheitIterator fItr = new FahrenheitIterator(tempsDAO.tempsVolume());

		Collection<Temp> c = new StreamingCollection(fItr);

		return new ResponseEntity<Collection<Temp>>(c, HttpStatus.OK);

	}

	@GetMapping("/temps/{id}")
	public ResponseEntity<TempScale> getTemp(@PathVariable(value = "id") String id) {

		TempScale temp = tempsDAO.get(new Integer(id));
		if (temp == null) {
			return new ResponseEntity<TempScale>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TempScale>(temp, HttpStatus.OK);
	}

	@PostMapping("/temps")
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Temp> createTemp(@RequestBody Celsius temp) {
		// @RequestBody is key for post - otherwise the body is null

		Temp idTemp = tempsDAO.create(temp);

		HttpHeaders headers = new HttpHeaders();
		// Change content type of response
		// headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		// headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<Temp>(idTemp, headers, HttpStatus.CREATED);
		// return idCust;
		// return Response.ok(idCust).build();
	}

	@PutMapping("/temps/{id}")
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TempScale> putTemp(@PathVariable(value = "id") String id, @RequestBody Celsius celsius) {

		TempScale rtnTemp = tempsDAO.update(new Integer(id), celsius);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		if (null == rtnTemp) {
			return new ResponseEntity<TempScale>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TempScale>(rtnTemp, headers, HttpStatus.OK);
	}
	
	@DeleteMapping("/temps/{id}")
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TempScale> deleteTemp(@PathVariable(value = "id") String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		TempScale rtnTemp = tempsDAO.delete( new Integer(id) );
		
		if (rtnTemp == null) {
			return new ResponseEntity<TempScale>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<TempScale>(rtnTemp, headers, HttpStatus.OK);
	}
	
	public static <T> T jsonAsObject(final String json, Class<T> cls) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final T type = mapper.readValue(json, cls);
			return type;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	 
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
