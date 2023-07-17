# eMODiTS
## Versión español
Discretizacion símbolica de bases de datos temporales usando optimización multi-objetivo y árboles de decisión.

Para ejecutar el programa, hay que posicionarse en la carpeta /TimeSeriesDiscretize y ejecutar el archivo CommandRunning.java o ejecutar el archivo eMODiTS.jar ubicado en la carpeta Ejecutable, de la siguiente manera:

  * java -jar eMODiTS.jar PS NE NG MR CR iFitnessFunction iAlgorithm iApproach iDS

Donde:
  * PS: Tamaño de la población
  * NE: Número de ejecuciones
  * NG: Número de generaciones
  * MR: Porcentaje de mutación
  * CR: Porcentaje de cruza
  * iFitnessFunction: Indica las funciones objetivo a utilizar (Ingresar la opción 8)
  * iAlgorithm: Indica el tipo de algoritmo a usar (Ingresar la opción 3)
  * iApproach: Indica el tipo de enfoque a utilizar (Ingresar la opción 1)
  * iDS: Este parámetro esta relacionado el número de base de datos que está definido en DataSets.java, puedes agregar un nuevo número, agregarlo ahí y pasarlo como parámetro en esta parte.

Ejemplo:
  * java -jar eMODiTS.jar 100 15 300 0.2 0.8 8 3 1 38

### Bases de datos
Las bases de datos empleadas son archivos de MATLAB que contienen los conjuntos de entrenamiento y prueba llamados <DATASET_NAME>_TRAIN y <DATASET_NAME>_TEST respectivamente, obtenidos del repositorio UCR [1]. 
La variable LIMITE se refiere a los valores superior e inferior encontrados en la base de datos, usados para fijar el dominio de las variables de decisión.
# eMODiTS
## English version
Symbolic Discretization of Time Series using Multi-Objective optimization and Decision Trees

To run the program, you must go to the /TimeSeriesDiscretize folder and execute the CommandRunning.java file or the eMODiTS.jar file located in the Executable folder, as follows:

  * java -jar eMODiTS.jar PS NE NG MR CR iFitnessFunction iAlgorithm iApproach iDS

Where:
  * PS: Population size
  * NE: Number of Executions
  * NG: Number of Generations
  * MR: Mutation Rate
  * CR: Crossover Rate
  * iFitnessFunction: Indicates the objective functions to use (Choose option 8)
  * iAlgorithm: Indicates the algorithm to employ (Choose option 3)
  * iApproach: Indicates the approach to utilize (Choose option 1)
  * iDS: This option is related to the database ID to be executed, defined in DataSets.java. Here, it is possible to add a new dataset by modifying the DataSet.java and after putting the new number as in this parameter.
  
Example:
  * java -jar eMODiTS.jar 100 15 300 0.2 0.8 8 3 1 38  

### Datasets
The employed datasets are MATLAB files containing the train and test data called <DATASET_NAME>_TRAIN and <DATASET_NAME>_TEST, respectively, obtained from the UCR repository [1]. 
LIMITS variable is the lower and upper value found in the datasets to fix the domain of the decision variables.

# References
[1] Chen, Yanping and Keogh, Eamonn and Hu, Bing and Begum, Nurjahan and Bagnall, Anthony and Mueen, Abdullah and Batista, Gustavo. The UCR Time Series Classification Archive. July 2015. www.cs.ucr.edu/~eamonn/time_series_data/.
  
