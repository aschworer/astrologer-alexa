package aschworer.astrologer.alexa.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aschworer
 */
public class NatalChart {

    private List<CharacteristicInSign> planets = new ArrayList<>();

    public NatalChart() {
    }

    public NatalChart(List<CharacteristicInSign> planets) {
        this.planets = planets;
    }

    public List<CharacteristicInSign> getPlanets() {
        return planets;
    }

    public void setPlanets(List<CharacteristicInSign> planets) {
        this.planets = planets;
    }

    public Sign getSign(Planet planet) {//todo optimise
        for (CharacteristicInSign c : planets) {
            if (c.getCharacteristic().equalsIgnoreCase(planet.getString())) {
                return Sign.getByString(c.getSign());
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "NatalChart{" +
                "planets=" + planets +
                '}';
    }
}
