package com.apap.tutorial3.service;

import java.util.List;
import com.apap.tutorial3.model.PilotModel;

/**
 * PilotService
 */
public interface PilotService {
    void addPilot(PilotModel pilot);

    List<PilotModel> getPilotList();

    PilotModel getPilotDetailByLicenseNumber(String licenseNumber);

    PilotModel getPilotDetailById(String id);
}

