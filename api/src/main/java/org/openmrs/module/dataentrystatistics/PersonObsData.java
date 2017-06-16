package org.openmrs.module.dataentrystatistics;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.Provider;

public class PersonObsData {

	protected final Log log = LogFactory.getLog(getClass());

	private Date dateCreated;
	private Provider provider;
	private Obs obs;
	private Person person;
	private Location location;

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Obs getObs() {
		return obs;
	}

	public void setObs(Obs obs) {
		this.obs = obs;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
