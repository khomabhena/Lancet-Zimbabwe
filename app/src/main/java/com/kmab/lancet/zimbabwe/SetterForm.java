package com.kmab.lancet.zimbabwe;

public class SetterForm {

    public SetterForm() {
    }

    private String key, uid, patientName, patientSurname, relationshipToMember, tissueSample,
            memberName, memberSurname, medicalAid, email, suffix, no, address, employer, phone, specimenType,
            medicalLink, formLink;
    private long patientDOB, timestamp;
    private boolean isSeen, isMale;

    public SetterForm(String key, String uid, String patientName, String patientSurname,
                      String relationshipToMember,  String tissueSample, String memberName, String memberSurname,
                      String medicalAid, String email, String suffix, String no, String address,
                      String employer, String phone, String specimenType, String medicalLink, String formLink,
                      long patientDOB, boolean isSeen, boolean isMale, long timestamp) {
        this.key = key;
        this.uid = uid;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.relationshipToMember = relationshipToMember;
        this.memberName = memberName;
        this.memberSurname = memberSurname;
        this.medicalAid = medicalAid;
        this.email = email;
        this.suffix = suffix;
        this.no = no;
        this.address = address;
        this.employer = employer;
        this.phone = phone;
        this.specimenType = specimenType;
        this.medicalLink = medicalLink;
        this.formLink = formLink;
        this.patientDOB = patientDOB;
        this.isSeen = isSeen;
        this.isMale = isMale;
        this.timestamp = timestamp;
        this.tissueSample = tissueSample;
    }

    public String getTissueSample() {
        return tissueSample;
    }

    public void setTissueSample(String tissueSample) {
        this.tissueSample = tissueSample;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getRelationshipToMember() {
        return relationshipToMember;
    }

    public void setRelationshipToMember(String relationshipToMember) {
        this.relationshipToMember = relationshipToMember;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberSurname() {
        return memberSurname;
    }

    public void setMemberSurname(String memberSurname) {
        this.memberSurname = memberSurname;
    }

    public String getMedicalAid() {
        return medicalAid;
    }

    public void setMedicalAid(String medicalAid) {
        this.medicalAid = medicalAid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    public String getMedicalLink() {
        return medicalLink;
    }

    public void setMedicalLink(String medicalLink) {
        this.medicalLink = medicalLink;
    }

    public String getFormLink() {
        return formLink;
    }

    public void setFormLink(String formLink) {
        this.formLink = formLink;
    }

    public long getPatientDOB() {
        return patientDOB;
    }

    public void setPatientDOB(long patientDOB) {
        this.patientDOB = patientDOB;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }
}
