package com.company;

import java.util.ArrayList;
import java.util.List;

public class VersionVector {
    private List<Version> versions;
    private Version localVersion;

    public VersionVector(String siteId) {
        this.localVersion = new Version(siteId);
        this.versions = new ArrayList<Version>();
        this.versions.add(localVersion);
    }

    public void increment() {
        this.localVersion.setCounter(this.localVersion.getCounter() + 1);
    }

    public void update(Version incomingVersion) {
        Version existingVersion = this.getVersionFromVector(incomingVersion);
        if (existingVersion == null) {
            Version newVersion = new Version(incomingVersion.getSiteId());
            newVersion.update(incomingVersion);
            this.versions.add(newVersion);
        } else {
            existingVersion.update(incomingVersion);
        }
    }

    public boolean hasBeenApplied(Version incomingVersion) {
        Version localIncomingVersion = this.getVersionFromVector(incomingVersion);
        boolean isIncomingInVersionVector = (localIncomingVersion != null);

        if (!isIncomingInVersionVector) return false;

        boolean isIncomingLower = incomingVersion.getCounter() <= localIncomingVersion.getCounter();
        boolean isInExceptions = localIncomingVersion.getExceptions().contains(incomingVersion.getCounter());

        return isIncomingLower && !isInExceptions;
    }

    public Version getVersionFromVector(Version version) {
        Version existingVersion = null;
        for (int i = 0; i < this.versions.size(); i++) {
            if (version.getSiteId().equals(this.versions.get(i).getSiteId())) {
                existingVersion = this.versions.get(i);
                break;
            }
        }
        return existingVersion;
    }

    public Version getLocalVersion() {
        return this.localVersion;
    }
}
