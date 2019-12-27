package dev.glinzac.automation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.glinzac.automation.entities.DatabaseEntity;

@Repository
public interface DatabaseRepository extends CrudRepository<DatabaseEntity, Integer> {
	
	@Query(value = "select * from db_entity where db_name like %?1% ",nativeQuery = true)
	List<DatabaseEntity> getDatabase(String db);
	
	@Query(value="select * from db_entity",nativeQuery = true)
	List<DatabaseEntity> getDatabaseList();
}

