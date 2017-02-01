package aschworer.astrologer.alexa.service.people;

/**
 * @author aschworer
 */
public interface PeopleService {

    void save(Person person);

    Person get(String name);
}
