package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;

public class ElemAttributeSet extends ElemUse {
    static final long serialVersionUID = -426740318278164496L;
    public QName m_qname = null;

    public void setName(QName name) {
        this.m_qname = name;
    }

    public QName getName() {
        return this.m_qname;
    }

    public int getXSLToken() {
        return 40;
    }

    public String getNodeName() {
        return "attribute-set";
    }

    /* JADX WARNING: type inference failed for: r1v3, types: [org.apache.xalan.templates.ElemTemplateElement] */
    /* JADX WARNING: Multi-variable type inference failed */
    public void execute(TransformerImpl transformer) throws TransformerException {
        if (!transformer.isRecursiveAttrSet(this)) {
            transformer.pushElemAttributeSet(this);
            super.execute(transformer);
            for (ElemAttribute attr = (ElemAttribute) getFirstChildElem(); attr != null; attr = attr.getNextSiblingElem()) {
                attr.execute(transformer);
            }
            transformer.popElemAttributeSet();
            return;
        }
        throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_XSLATTRSET_USED_ITSELF, new Object[]{this.m_qname.getLocalPart()}));
    }

    public ElemTemplateElement appendChildElem(ElemTemplateElement newChild) {
        if (newChild.getXSLToken() != 48) {
            error(XSLTErrorResources.ER_CANNOT_ADD, new Object[]{newChild.getNodeName(), getNodeName()});
        }
        return super.appendChild(newChild);
    }

    public void recompose(StylesheetRoot root) {
        root.recomposeAttributeSets(this);
    }
}
