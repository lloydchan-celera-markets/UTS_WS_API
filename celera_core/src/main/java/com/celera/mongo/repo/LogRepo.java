package com.celera.mongo.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.celera.mongo.entity.Log;

public interface LogRepo extends CrudRepository<Log, Long>
{
//	@Query("{'lastModified' : { '$gte' : new ISODate(?0), '$lt' : new ISODate(?1) }}")
	@Query(value = "{'lastModified' : { '$gte' : ?0, '$lt' : ?1 }}")
	public Iterable<Log> getBetween(Date start, Date end);
//	public Iterable<Log> getBetween(String start, String end);
//	public List<Log> findByLastModified(@Temporal(TemporalType.Date) Date start, @Temporal(TemporalType.Date) String end);

}
