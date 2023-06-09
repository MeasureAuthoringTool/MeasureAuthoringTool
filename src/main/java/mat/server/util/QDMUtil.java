package mat.server.util;

import mat.client.shared.QDMContainer;
import org.cqframework.cql.cql2elm.qdm.QdmModelInfoProvider;
import org.hl7.cql.model.ModelIdentifier;
import org.hl7.elm_modelinfo.r1.ChoiceTypeSpecifier;
import org.hl7.elm_modelinfo.r1.ClassInfo;
import org.hl7.elm_modelinfo.r1.ClassInfoElement;
import org.hl7.elm_modelinfo.r1.ListTypeSpecifier;
import org.hl7.elm_modelinfo.r1.ModelInfo;
import org.hl7.elm_modelinfo.r1.NamedTypeSpecifier;
import org.hl7.elm_modelinfo.r1.ProfileInfo;
import org.hl7.elm_modelinfo.r1.TypeInfo;
import org.hl7.elm_modelinfo.r1.TypeSpecifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QDMUtil {

    private static final QdmModelInfoProvider qdmModelInfoProvider = new QdmModelInfoProvider();

    private QDMUtil() {
        throw new IllegalStateException();
    }

    public static QDMContainer getQDMContainer() {

        QDMContainer qdmContainer = new QDMContainer();
        ModelIdentifier modelIdentifier = new ModelIdentifier();
        modelIdentifier.setId("QDM");
        modelIdentifier.setVersion("5.6");
        ModelInfo modelInfo = qdmModelInfoProvider.load(modelIdentifier);

        Map<String, TypeInfo> nameToProfileInfoMap = new HashMap<>();
        Map<String, ClassInfo> nameToClassInfoMap = new HashMap<>();

        // pre-process this information to be more efficient
        buildProfileAndClassInfoMaps(modelInfo, nameToProfileInfoMap, nameToClassInfoMap);

        Map<String, List<String>> dataTypeToAttributeMap = new HashMap<>();
        Map<String, List<String>> attributeToCQLTypeMap = new HashMap<>();
        // create the data type label --> attribute map
        nameToProfileInfoMap.forEach((k, v) -> {
            String label = ((ClassInfo) v).getLabel(); // e.g. Encounter, Performed
            String baseType = v.getBaseType(); // e.g. QDM.EncounterPerformed

            // this accounts for elements that are not a profile
            // for example QDM.CareGoal is not a profile and has a base type of QDM.QDMBaseType
            // we still want to collect information about it, but we do not want attributes for the base type, but
            // rather we want attributes for the current datatype so we will use it's name (QDM.CareGoal) so that
            // we can look up information about the current datatypes attribute.

            // We only want to use base type information if it is a profile. For example, QDM.PositiveEncounterPerformed
            // has a base type of QDM.EncounterPerformed. We can than use this base type to look up the attributes for
            // that datatype.
            if (baseType.equals("QDM.QDMBaseType")) {
                baseType = ((ClassInfo) v).getName();
            }

            ClassInfo classInfo = nameToClassInfoMap.get(baseType);
            if (!dataTypeToAttributeMap.containsKey(label)) {
                dataTypeToAttributeMap.put(label, new ArrayList<>());
            }

            List<ClassInfoElement> attributesForDataType = new ArrayList<>();
            attributesForDataType.addAll(classInfo.getElement()); // adds data type specific attributes
            attributesForDataType.addAll(nameToClassInfoMap.get("QDM.QDMBaseType").getElement()); // add base type attributes

            if (classInfo.getElement() != null) {
                for (ClassInfoElement attribute : attributesForDataType) {
                    dataTypeToAttributeMap.get(label).add(attribute.getName());
                    collectAttributeTypeInformation(attributeToCQLTypeMap, label, attribute);
                }
            }
        });

        qdmContainer.setDatatypeToAttributesMap(dataTypeToAttributeMap);
        qdmContainer.setQdmAttributeToTypeMap(attributeToCQLTypeMap);
        return qdmContainer;
    }

    private static void buildProfileAndClassInfoMaps(ModelInfo modelInfo, Map<String, TypeInfo> nameToProfileInfoMap,
                                                     Map<String, ClassInfo> nameToClassInfoMap) {
        List<TypeInfo> typeInfoList = modelInfo.getTypeInfo();
        for (TypeInfo typeInfo : typeInfoList) {

            if (typeInfo instanceof ProfileInfo) {
                ProfileInfo currentProfileInfo = (ProfileInfo) typeInfo;
                nameToProfileInfoMap.put(currentProfileInfo.getName(), currentProfileInfo);
            }

            if (typeInfo instanceof ClassInfo) {
                ClassInfo currentClassInfo = (ClassInfo) typeInfo;
                nameToClassInfoMap.put(currentClassInfo.getName(), currentClassInfo);

                if (currentClassInfo.getLabel() != null) {
                    nameToProfileInfoMap.put(currentClassInfo.getName(), currentClassInfo);
                }
            }
        }
    }

    private static void collectAttributeTypeInformation(Map<String, List<String>> attributeToCQLTypeMap, String label,
                                                        ClassInfoElement attribute) {
        if (!attributeToCQLTypeMap.containsKey(label)) {
            attributeToCQLTypeMap.put(attribute.getName(), new ArrayList<>());
        }

        if (attribute.getTypeSpecifier() != null) {
            // handles the case where the attribute has a type of choice type specifier
            getAttributesFromChoiceTypeSpecifier(attributeToCQLTypeMap, attribute);
        } else {
            // handle the case where it is a normal attribute
            // we shouldn't put clarifying attributes for System.Code
            attributeToCQLTypeMap.get(attribute.getName()).add(attribute.getType());
        }
    }

    private static void getAttributesFromChoiceTypeSpecifier(Map<String, List<String>> attributeToCQLTypeMap,
                                                             ClassInfoElement attribute) {

        ChoiceTypeSpecifier choiceTypeSpecifier = getChoiceTypeSpecifier(attribute);
        for (TypeSpecifier typeSpecifier : choiceTypeSpecifier.getChoice()) {
            NamedTypeSpecifier namedTypeSpecifier = (NamedTypeSpecifier) typeSpecifier;
            String formattedType = namedTypeSpecifier.getModelName() + "." + namedTypeSpecifier.getName();

            // we shouldn't put clarifying attributes for System.Code
            attributeToCQLTypeMap.get(attribute.getName()).add(formattedType);
        }
    }

    private static ChoiceTypeSpecifier getChoiceTypeSpecifier(ClassInfoElement attribute) {

        if (attribute.getTypeSpecifier() instanceof ListTypeSpecifier) {
            ListTypeSpecifier listTypeSpecifier = (ListTypeSpecifier) attribute.getTypeSpecifier();
            return (ChoiceTypeSpecifier) listTypeSpecifier.getElementTypeSpecifier();
        } else {
            return (ChoiceTypeSpecifier) attribute.getTypeSpecifier();
        }
    }
}
