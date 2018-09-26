package com.apap.tutorial3.controller;

import com.apap.tutorial3.service.PilotService;
import com.apap.tutorial3.model.PilotModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class PilotController {

    private final PilotService pilotService;

    @Autowired
    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @RequestMapping("/pilot/add")
    public String add(@RequestParam(value = "id") String id,
                      @RequestParam(value = "licenseNumber") String licenseNumber,
                      @RequestParam(value = "name") String name,
                      @RequestParam(value = "flyHour") Integer flyHour) {

        PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
        pilotService.addPilot(pilot);
        return "add";
    }

    @RequestMapping("/pilot/view")
    public String view(@RequestParam(value = "licenseNumber", required = false) String licenseNumber, Model model) {

        if (licenseNumber != null) {
            PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);

            if (archive != null) {
                model.addAttribute("pilot", archive);
                return "view-pilot";
            } else {
                String errMsg = "Pilot dengan License Number: " + licenseNumber + " tidak ditemukan";
                model.addAttribute("err", errMsg);
                return "error-page";
            }
        }

        String errMsg = "Pilot tidak berhasil ditemukan";
        model.addAttribute("err", errMsg);
        return "error-page";

    }

    @RequestMapping("/pilot/viewall")
    public String viewall(Model model) {
        List<PilotModel> archive = pilotService.getPilotList();
        model.addAttribute("listPilot", archive);
        return "viewall-pilot";
    }

    @RequestMapping("/pilot/view/licenseNumber/{licenseNumber}")
    public String viewPilot(@PathVariable Optional<String> licenseNumber, Model model) {
        PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());

        if (archive == null) {
            String errMsg = "Pilot dengan License Number: " + licenseNumber.get() + " tidak ditemukan";
            model.addAttribute("err", errMsg);
            return "error-page";
        }
        model.addAttribute("pilot", archive);
        return "view-pilot";
    }

    @RequestMapping(value = {"pilot/update/license-number/{licenseNumber}/fly-hour/{newFlyHour}",
            "/pilot/update/license-number/fly-hour",
            "/pilot/update/license-number/{licenseNumber}/fly-hour",
            "/pilot/update/license-numer/fly-hour/{newFlyHour}"})
    public String updateAmount(@PathVariable Optional<String> licenseNumber,
                               @PathVariable Optional<String> newFlyHour,
                               Model model) {

        String errMsg;
        if (licenseNumber.isPresent()) {
            PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());

            if (newFlyHour.isPresent() && archive != null) {
                archive.setFlyHour(Integer.parseInt(newFlyHour.get()));
                return "update";
            } else if (!newFlyHour.isPresent()) {
                errMsg = "Fly Hour tidak boleh kosong";
                model.addAttribute("err", errMsg);
                return "error-page";
            }
            errMsg = "Pilot dengan License Number: " + licenseNumber.get() + " tidak ditemukan";
            model.addAttribute("err", errMsg);
            return "error-page";
        }
        errMsg = "License Number Pilot tidak boleh kosong";
        model.addAttribute("err", errMsg);
        return "error-page";
    }

    @RequestMapping(value = {"/pilot/delete/id/{id}", "/pilot/delete/id"})
    public String delete(@PathVariable Optional<String> id, Model model) {
        String errMsg;

        if (id.isPresent()) {
            PilotModel pilot = pilotService.getPilotDetailById(id.get());
            if (pilot != null) {
                pilotService.getPilotList().remove(pilot);
                return "delete";
            }
            errMsg = "Pilot dengan ID: " + id.get() + " tidak ditemukan";
            model.addAttribute("err", errMsg);
            return "error-page";
        }
        errMsg = "ID Pilot tidak boleh kosong";
        model.addAttribute("err", errMsg);
        return "error-page";
    }
}
