package ParetoFront;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.IScheme;
import ca.nengo.io.MatlabExporter;

public class ParetoFrontCollection {
    public List<ParetoFront> frentes;

    public ParetoFrontCollection(){
        frentes = new ArrayList<>();
    }

    public void add(ParetoFront pf){
        frentes.add(pf);
    }

    public void Export(String directory){
        String dir = directory+"/ExecutionFronts/";
        for(int i=0;i<this.frentes.size();i++){
            MatlabExporter exporter = new MatlabExporter();
            exporter.add("FitnessFunctions", this.frentes.get(i).toFloatArray());
            for(int j=0;j<this.frentes.get(i).getFront().size();j++){
                IScheme esquema = this.frentes.get(i).getFront().get(j);
                float[][] data = esquema.Elements2FloatArray();
                exporter.add("IndividualFront"+j, data);
            }
            File FileDir = new File(dir);
            if(!FileDir.exists()) FileDir.mkdirs();
            File FileTabla = new File(dir+"FrontExecution"+i+".mat");
            try {
                exporter.write(FileTabla);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
