package uk.ac.ebi.pridemod.controller.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pridemod.controller.AbstractDataAccessController;
import uk.ac.ebi.pridemod.io.pridemod.model.PrideMod;
import uk.ac.ebi.pridemod.io.pridemod.model.PrideModification;
import uk.ac.ebi.pridemod.io.pridemod.xml.PrideModReader;
import uk.ac.ebi.pridemod.model.PRIDEModPTM;
import uk.ac.ebi.pridemod.model.PTM;
import uk.ac.ebi.pridemod.model.Specificity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * yperez
 */
public class PRIDEModDataAccessController extends AbstractDataAccessController {

    private static final Logger logger = LoggerFactory.getLogger(PRIDEModDataAccessController.class);

    /**
     * Default constructor for Controllers
     *
     * @param xml
     */
    public PRIDEModDataAccessController(InputStream xml) {
        super(xml);

        PrideModReader reader = new PrideModReader(xml);
        initPTMMap(reader.getPrideMod());
    }

    /**
     * Init the PTM map.
     * @param prideMod
     */
    private void initPTMMap(PrideMod prideMod) {
        if(prideMod != null) {
            ptmMap = new HashMap<Comparable, PTM>();
            for (PrideModification oldMod : prideMod.getPrideModifications().getPrideModification()) {
                final String accession = oldMod.getId();
                final String name = oldMod.getTitle();
                final Double monoMass = oldMod.getDiffMono().doubleValue();
                final List<Specificity> specicityList = oldMod.getSpecificityList();
                //TODO: Change bioSignificance to boolean?
                //In principle is safe to convert from BigInteger to int becasue the values are 0 and 1
                final boolean bioSignificance = oldMod.getBiologicalSignificance().intValue() ==  1;
                Comparable unimodReference = String.valueOf(oldMod.getUnimodMappings().getUnimodMapping().get(0).getId().intValue());
                PRIDEModPTM ptm = new PRIDEModPTM(accession, name, name, monoMass, null, specicityList, unimodReference, null, bioSignificance);
                ptmMap.put(accession, ptm);
            }
        }
    }

}
