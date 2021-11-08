
package it.prova.gestionesatelliti.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	private SatelliteRepository repository;

	// questo mi serve per il findByExample2 che risulta 'a mano'
	// o comunque in tutti quei casi in cui ho bisogno di costruire custom query nel
	// service
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite impiegatoInstance) {
		repository.save(impiegatoInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite impiegatoInstance) {
		repository.save(impiegatoInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Satellite impiegatoInstance) {
		repository.delete(impiegatoInstance);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findByExample(Satellite example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select i from Satellite i where i.id = i.id ");

		if (StringUtils.isNotEmpty(example.getDenominazione())) {
			whereClauses.add(" i.denominazione  like :denominazione ");
			paramaterMap.put("denominazione", "%" + example.getDenominazione() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCodice())) {
			whereClauses.add(" i.codice like :codice ");
			paramaterMap.put("codice", "%" + example.getCodice() + "%");
		}

		if (example.getStato() != null) {
			whereClauses.add(" i.stato =:stato ");
			paramaterMap.put("stato", example.getStato());
		}
		if (example.getDataRientro() != null) {
			whereClauses.add("i.dataRientro >= :dataRientro ");
			paramaterMap.put("dataRientro", example.getDataRientro());
		}
		if (example.getDataLancio() != null) {
			whereClauses.add("i.dataLancio >= :dataLancio ");
			paramaterMap.put("dataLancio", example.getDataLancio());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Satellite> typedQuery = entityManager.createQuery(queryBuilder.toString(), Satellite.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

}
