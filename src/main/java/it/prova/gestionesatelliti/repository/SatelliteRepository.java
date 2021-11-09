package it.prova.gestionesatelliti.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long> {
	
	List<Satellite> findAllByDataLancioLessThanEqualAndStatoNot(Date dataAttuale,
			String statoDisattivato);

	
}
