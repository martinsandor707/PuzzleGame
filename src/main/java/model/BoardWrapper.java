package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This wrapper class is needed because apparently JAXB can't handle Collections of Objects.
 * I know the people who created JAXB are much smarter than me, but can't they just make it look for the class of the
 * object that is stored in the Collection?
 */
@XmlRootElement (name="board")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class BoardWrapper {
    @XmlElement (name="cell")
    private List<Cell> board=new ArrayList<>();
}
