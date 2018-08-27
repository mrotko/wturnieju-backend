package pl.wturnieju.model.generic;

import pl.wturnieju.model.Persistent;
import pl.wturnieju.model.ProfileType;

public abstract class GenericProfile extends Persistent {

    protected String name;

    protected String img;

    protected ProfileType profileType;

    protected String description;
}
