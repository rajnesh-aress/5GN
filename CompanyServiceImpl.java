package com.cmdb.integration.service;

import com.cmdb.integration.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setSiteId("a81255c5-75bc-4368-a83c-d95a0a93db29");
        companies.add(company);
        return companies;
    }
}
