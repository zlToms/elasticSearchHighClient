package com.tom.tz.controller;

import com.tom.tz.entity.ESEntity;
import com.tom.tz.es.ImportDataES;
import com.tom.tz.es.ImportDataES02;
import com.tom.tz.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/elasticsearch")
public class ImportData {

    @Autowired
    private ImportDataES02 importDataES02;



    @RequestMapping("/import")
    public @ResponseBody String importData() throws IOException {
        importDataES02.bulkImportToES();
        return "ok!!!!";
    }

    @RequestMapping("/delete")
    public @ResponseBody String deleteIndex() throws IOException {
        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("posts");
        importDataES02.deleteDataES(esEntity);
        return "ok!!!!";
    }


    @RequestMapping("/import2Database")
    public @ResponseBody String importData2Database() throws IOException {
        new DataUtils().bathInsert();
        return "ok!!!!";
    }
}
