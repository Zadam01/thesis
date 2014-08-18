package org.checkerframework.qualframework.base;

import javax.lang.model.type.TypeKind;

/** Default implementation of {@link TypeHierarchy}.  Applies standard Java
 * subtyping rules to compare Java types, and uses a {@link QualifierHierarchy}
 * to compare qualifiers.
 */
public class DefaultTypeHierarchy<Q> implements TypeHierarchy<Q> {
    // private final QualifierHierarchy<Q> qualifierHierarchy;
    private TypeHierarchyAdapter<Q> adapter;

    /**
     * @param qualifierHierarchy
     *      a {@link QualifierHierarchy} to use for comparing individual qualifiers
     */
    public DefaultTypeHierarchy(QualifierHierarchy<Q> qualifierHierarchy) {
        // this.qualifierHierarchy = qualifierHierarchy;
    }

    void setAdapter(TypeHierarchyAdapter<Q> adapter) {
        this.adapter = adapter;
    }


    @Override
    public boolean isSubtype(QualifiedTypeMirror<Q> subtype, QualifiedTypeMirror<Q> supertype) {
        // Qualifier API doesn't allow qualifiers on NullType, but the
        // underlying framework does (and expects the annotations to match).
        // So we have this extra check to make NullType a subtype of all
        // reference types.
        if (subtype.getKind() == TypeKind.NULL) {
            switch (supertype.getKind()) {
                case ARRAY:
                case DECLARED:
                case NULL:
                case TYPEVAR:
                    return true;
                default:
            }
        }

        return adapter.superIsSubtype(subtype, supertype);
    }
}
