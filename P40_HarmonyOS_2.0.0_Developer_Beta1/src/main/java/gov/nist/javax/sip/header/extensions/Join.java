package gov.nist.javax.sip.header.extensions;

import gov.nist.core.Separators;
import gov.nist.javax.sip.header.CallIdentifier;
import gov.nist.javax.sip.header.ParameterNames;
import gov.nist.javax.sip.header.ParametersHeader;
import java.text.ParseException;
import javax.sip.header.ExtensionHeader;

public class Join extends ParametersHeader implements ExtensionHeader, JoinHeader {
    public static final String NAME = "Join";
    private static final long serialVersionUID = -840116548918120056L;
    public String callId;
    public CallIdentifier callIdentifier;

    public Join() {
        super("Join");
    }

    public Join(String callId2) throws IllegalArgumentException {
        super("Join");
        this.callIdentifier = new CallIdentifier(callId2);
    }

    @Override // gov.nist.javax.sip.header.ParametersHeader, gov.nist.javax.sip.header.SIPHeader
    public String encodeBody() {
        if (this.callId == null) {
            return null;
        }
        String retVal = this.callId;
        if (this.parameters.isEmpty()) {
            return retVal;
        }
        return retVal + Separators.SEMICOLON + this.parameters.encode();
    }

    @Override // gov.nist.javax.sip.header.extensions.JoinHeader
    public String getCallId() {
        return this.callId;
    }

    public CallIdentifier getCallIdentifer() {
        return this.callIdentifier;
    }

    @Override // gov.nist.javax.sip.header.extensions.JoinHeader
    public void setCallId(String cid) {
        this.callId = cid;
    }

    public void setCallIdentifier(CallIdentifier cid) {
        this.callIdentifier = cid;
    }

    @Override // gov.nist.javax.sip.header.extensions.JoinHeader
    public String getToTag() {
        if (this.parameters == null) {
            return null;
        }
        return getParameter(ParameterNames.TO_TAG);
    }

    @Override // gov.nist.javax.sip.header.extensions.JoinHeader
    public void setToTag(String t) throws ParseException {
        if (t == null) {
            throw new NullPointerException("null tag ");
        } else if (!t.trim().equals("")) {
            setParameter(ParameterNames.TO_TAG, t);
        } else {
            throw new ParseException("bad tag", 0);
        }
    }

    public boolean hasToTag() {
        return hasParameter(ParameterNames.TO_TAG);
    }

    public void removeToTag() {
        this.parameters.delete(ParameterNames.TO_TAG);
    }

    @Override // gov.nist.javax.sip.header.extensions.JoinHeader
    public String getFromTag() {
        if (this.parameters == null) {
            return null;
        }
        return getParameter(ParameterNames.FROM_TAG);
    }

    @Override // gov.nist.javax.sip.header.extensions.JoinHeader
    public void setFromTag(String t) throws ParseException {
        if (t == null) {
            throw new NullPointerException("null tag ");
        } else if (!t.trim().equals("")) {
            setParameter(ParameterNames.FROM_TAG, t);
        } else {
            throw new ParseException("bad tag", 0);
        }
    }

    public boolean hasFromTag() {
        return hasParameter(ParameterNames.FROM_TAG);
    }

    public void removeFromTag() {
        this.parameters.delete(ParameterNames.FROM_TAG);
    }

    @Override // javax.sip.header.ExtensionHeader
    public void setValue(String value) throws ParseException {
        throw new ParseException(value, 0);
    }
}
