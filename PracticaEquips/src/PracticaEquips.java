import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class PracticaEquips {
    static final String FITXERSDIR = "fitxer/";
    static Scanner teclat = new Scanner(System.in);
    
    static String[] equips = new String[100];
    static int[][] puntuacions = new int[100][5];
    
    static int ultimEquip = 0;

    static FileWriter fw = null;
    static BufferedWriter bw = null;
    static PrintWriter pw = null;
    public static void main(String[] args) throws IOException {
        
        llegirFitxers();

        boolean sortir = false;

        do {
            System.out.println("\n-----Gestió lliga esportiva-----");
            System.out.println("1. Visualitzar puntuacions");
            System.out.println("2. Afegir nou equip");
            System.out.println("3. Modificar puntuacions");
            System.out.println("4. Visualitzar líder i cuer");
            System.out.println("5. Sortir");
            System.out.print("\nTRIA UNA OPCIÓ: ");

            int opcio;

            while (true)
            try {
                opcio = Integer.parseInt(teclat.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("TRIA UNA OPCIÓ ");
            }

            switch (opcio) {
            case 1:
                visualitzarPuntuacions();
                break;
            case 2:
                afegirEquip();
                break;
            case 3:
                modificarPuntuacions();
                break;
            case 4:
                visualitzarLider();
                break;
            case 5:
                guardarPuntuacionsFitxer();
                sortir = true;
                break;
            default:
                System.out.println("VALOR NO VÀLID");
                break;
            }
        } while (!sortir);
    }

    static void llegirFitxers() throws IOException {

        File fitxer = new File(FITXERSDIR);

        fitxer.mkdirs();

        if (fitxer.isDirectory()) {
            File[] fitxers = fitxer.listFiles();
            for (int i = 0; i < fitxers.length; i++) {
                actualitzarArrays(fitxers[i]);
            }
        }
    }

    static void actualitzarArrays(File fitxer) throws NumberFormatException, IOException {
        int i = 0;
        FileReader reader = new FileReader(fitxer);
        
        BufferedReader buffer = new BufferedReader(reader);

        String linea;

        //Bucle per afegir les puntuacions i equips del fitxer puntuacions.txt als arrays corresponents.
        while((linea = buffer.readLine()) != null) {
            int p = 0;
            int pos = 1;
            String[] separador = linea.split(":");
            equips[i] = separador[0];

            while((pos < separador.length)) {
                puntuacions[i][p] = Integer.parseInt(separador[pos]);
                p++;
                pos++;
            }
            i++;
        }
        calcularPuntuacions();
        reader.close();
        buffer.close();
    }

    static void calcularPuntuacions() {
        //Bucle per calcular els punts de cada equip i afegir-los a l'array 'puntuacions'.
        int i = 0;
        int ultimEquip = Arrays.asList(equips).indexOf(null);
        
        int punts = 0;
        for (i = 0; i < ultimEquip; i++) {
            for (int j = 0; j < puntuacions[i].length;j++){   
                if (j == 1) {
                    punts += puntuacions[i][j] * 3;
                }
                if (j == 2) {
                    punts += puntuacions[i][j] * 1;
                }
            }
            puntuacions[i][4] = punts;
            punts = 0;
        }
    }

    static void visualitzarPuntuacions() {
        int i = 0;
        //Busquem el primer valor null a l'array equips, així sabem quan hem de parar de recorrer les puntuacions.
        int ultimEquip = Arrays.asList(equips).indexOf(null);

        //Bucle que recorre les puntuacions de cada equip.
        System.out.printf("\n%20s %20s %20s %20s %20s %20s", "Equip", "Partits jugats", "Partits guanyats", "Partits empatats", "Partits perduts", "Punts\n");

            for (i=0; i < ultimEquip;i++){
                System.out.printf("%20s", equips[i]);
                for (int j=0; j<puntuacions[i].length;j++){
                    
                    System.out.printf("%20d", puntuacions[i][j]);
                }
            System.out.println();
            }
    }

    static void afegirEquip() {
        System.out.println("Escriu el nom de l'equip que vols afegir: ");
        String nom = teclat.next();
        System.out.println("Escriu els partits jugats: "); 
        int pj = teclat.nextInt();
        System.out.println("Escriu els partits guanyats: ");
        int pg = teclat.nextInt();
        System.out.println("Escriu els partits empatats: ");
        int pe = teclat.nextInt();
        System.out.println("Escriu els partits perduts: ");
        int pp = teclat.nextInt();

        if ( pj == pg + pe + pp ) {
            int ultimEquip = Arrays.asList(equips).indexOf(null);
            puntuacions[ultimEquip][0] = pj;
            puntuacions[ultimEquip][1] = pg;
            puntuacions[ultimEquip][2] = pe;
            puntuacions[ultimEquip][3] = pp;
            
            equips[ultimEquip] = nom;
            calcularPuntuacions();
            System.out.println("\nS'ha afegit l'equip correctament!");
        } else {
            System.out.println("\nEls partits jugats han de ser igual a la suma dels partits guanyats, empatats i perduts!");
        }

    }

    static void modificarPuntuacions() {

        int ultimEquip = Arrays.asList(equips).indexOf(null);

        System.out.println("Quin equip vols modificar?\n");
        
        int i = 0;
        while ( i < ultimEquip) {
            //Sumo 1 a la i per a que començi a contar els equips a partir de l'1.
            System.out.println( i + 1 + ".) " + equips[i] );
            i++;
        }

        int modEquip = teclat.nextInt();
        int pj = puntuacions[modEquip - 1][0];
        int pg = puntuacions[modEquip - 1][1];
        int pe = puntuacions[modEquip - 1][2];
        int pp = puntuacions[modEquip - 1][3];

        System.out.println("Estas modificant l'equip: " + equips[modEquip - 1] + " (Deixa en blanc si no vols modificar)");

        //Guardo els partits en variables String per poder comprovar si són null 
        //si són null es quedarà el valor que hi havia, si no es null es passarà a int i substituirà el valor.
        System.out.print("Introdueix els partits jugats: ");
        teclat.nextLine();
        String pjString = teclat.nextLine();
        if (!pjString.equals("")) {
            pj = Integer.parseInt(pjString);
        }
        
        System.out.print("Introdueix els partits guanyats: ");
        String pgString = teclat.nextLine();
        if (!pgString.equals("")) {
            pg = Integer.parseInt(pgString);
        }

        System.out.print("Introdueix els partits empatats: ");
        String peString = teclat.nextLine();
        if (!peString.equals("")) {
            pe = Integer.parseInt(peString);
        }

        System.out.print("Introdueix els partits perduts: ");
        String ppString = teclat.nextLine();
        if (!ppString.equals("")) {
            pp = Integer.parseInt(ppString);
        }

        if ( pj == pg + pe + pp ) {
            puntuacions[modEquip - 1][0] = pj;
            puntuacions[modEquip - 1][1] = pg;
            puntuacions[modEquip - 1][2] = pe;
            puntuacions[modEquip - 1][3] = pp;
            calcularPuntuacions();
            System.out.println("S'ha modificat l'equip correctament!");
        } else {
            System.out.println("\nEls partits jugats han de ser igual a la suma dels partits guanyats, empatats i perduts!");
        }

        
    }

    static void visualitzarLider() {
        int ultimEquip = Arrays.asList(equips).indexOf(null);

        int i = 0;

        int max = puntuacions[0][4];
        int equipMax = 0;

        while ( i < ultimEquip ) {
            if ( puntuacions[i][4] > max ) {
            max = puntuacions[i][4];
            equipMax = i;
            }
        i++;
        }

        i = 0;
        int numLiders = 0;
        //En cas que hi hagi més d'un equip empatat al primer lloc es guarden a l'array equipsLiders.
        String[] equipsLiders = new String[100];
        while ( i < ultimEquip ) {
            if ( puntuacions[i][4] == max ) {
                numLiders = Arrays.asList(equipsLiders).indexOf(null);
                equipsLiders[numLiders] = equips[i];
            }
        i++;
        }

        i = 0;
        //Si detecta que hi ha més d'un equip empatat en primer lloc s'imprimirà en plural, si detecta que només n'hi ha un s'imprimirà en singular.
        if (numLiders > 0) {
            System.out.print("Els equips líders de la lliga són: ");
            while ( i <= numLiders ) {
                System.out.print(equipsLiders[i] + ", ");
                i++;
            }
            System.out.println("amb " + max + " punts.");
        } else {
            System.out.println("L'equip " + equips[equipMax] + " va primer a la lliga amb " + max + " punts.");
        }
        

        int minim = puntuacions[0][4];
        int equipMin = 0;

        while ( i < ultimEquip ) {
            if ( puntuacions[i][4] < minim ) {
            minim = puntuacions[i][4];
            equipMin = i;
            }
        i++;
        }

        i = 0;
        int numCuers = 0;
        //En cas que hi hagi més d'un equip empatat en última posició es guarden en l'array equipsCuers.
        String[] equipsCuers = new String[100];
        while ( i < ultimEquip ) {
            if ( puntuacions[i][4] == minim ) {
                numCuers = Arrays.asList(equipsCuers).indexOf(null);
                equipsCuers[numCuers] = equips[i];
            }
        i++;
        }

        i = 0;
        //Si detecta que hi ha més d'un equip empatat en últim lloc s'imprimirà en plural, si detecta que només n'hi ha un s'imprimirà en singular.
        if (numCuers > 0) {
            System.out.print("Els equips cuers de la lliga són: ");
            while ( i <= numCuers ) {
                System.out.print(equipsCuers[i] + ", ");
                i++;
            }
            System.out.println("amb " + minim + " punts.");
        } else {
            System.out.println("L'equip " + equips[equipMin] + " va últim a la lliga amb " + minim + " punts.");
        }
        
    }

    static void guardarPuntuacionsFitxer() throws IOException {
        FileWriter fw = new FileWriter(FITXERSDIR + "puntuacions.txt", false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        int ultimEquip = Arrays.asList(equips).indexOf(null);
        int i = 0;

        while ( i < ultimEquip ) {
            int p = 0;
            pw.print(equips[i]);
            while ( p < puntuacions[i].length) {
                pw.print( ":" + puntuacions[i][p]);
                p++;
            }
        pw.println();
        i++;
        }
        
        pw.close();
    }

}
