/*
Started May 7
Made by Allen ---, Sebastian ---------, Maria Mathai

Project for Nyla ------
Task is to create a class discussion organizer that can track when and for how long students speak for, as well as overarching data on their
participation in class

*/

import javax.swing.*; // Visuals
import java.awt.*; // AWT for quite a few things
import javax.swing.border.LineBorder; // for the graphics display's border
import java.awt.event.*; // events for buttons
// File reading/writing stuff
//error checker
import java.io.*; // for csvs for saving and loading
//makes arrayslists
//makes arrays 
import java.util.*; // util for quite a few things
import java.util.List; // believe it or not, makes lists. Redefining these because sometimes code gets confused when both util and awt have "list" datatype

public class P1Main extends JFrame { // extending jframe so we can just do add() instead of frame.add()
    // Making these publically accessible so we can call them inside functions (cuz we call panel a lot so it would be a pain to have to add it in every function etc.)
    public static JPanel panel = new JPanel(new GridLayout(8, 1,4,4)); // main panel, setting it as a grid that's 8 tall, 1 wide, with 4 buffer pixels on horizontal and vertical
    public static JMenuBar menuBar = new JMenuBar(); // Menu bar at top
    public static List<String> headeri = new ArrayList<>();  //list composed of string, headeri
    public static List<List<String>> datai = new ArrayList<>(); //list composed of string lists, datai
    public static int b=-13; // to be used for saving person's individual teacher notes/comments
    
    private static final Color BACKGROUND_COLOR = Color.decode("#F7F9F9"); // color stuff
    private static final Color BUTTON_COLOR = Color.decode("#BED8D4");
    private static final Color BORDER_COLOR = Color.decode("#2081C3");
    
    // --> timer logic definitions
    private long startTime = 0;
    private long endTime = 0;
    boolean timerRunning = false;
    private long duration = 0;
    private List<Double> durations = new ArrayList<>(); // list of durations
    // <-- end timer logic defs
    
    public P1Main() { 
        super(); // Since this is the definer for the P1Main class that extends JFrame, obligatory super to follow the absolute programming law
        
        // Layout
        setSize(400, 400); // sets dimensions of the screen to 400x400 pixels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // obligatory thing that tells it what to do when program closes
        
        // JMenu stuff
        // Create JMenuItems (menu items)
        JMenuItem home = new JMenuItem("Home"); // home button and quit button
        JMenuItem quit = new JMenuItem("Quit");
        
        // triggers for when the two buttons are pressed
        home.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                panel = new JPanel(new GridLayout(8, 1,4,4)); // if home is pressed, reset the panel back to default grid layout dimensions as defined above
                panel.setBackground(BACKGROUND_COLOR); // change background color back to what it was
                //basically just a pile of functions that wipes and recompiles screen
                getContentPane().removeAll(); // getting content pane and removing everything from it is just removing everything like the panels etc. that were put in there
                panel.removeAll(); // clear panel
                paintHome(); // function to draw home defined elsewhere
                add(panel); // puts main panel back in
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener
        
        quit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              System.exit(0); // just kills the program when quit pressed
            } // end actionPerformed 
        }); // end actionlistener
        
        // add to screen
        menuBar.add(home); //add the stuff to menubar
        menuBar.add(quit); //add the stuff to menubar
        setJMenuBar(menuBar); // set menu bar into the frame
        
        panel.setBackground(BACKGROUND_COLOR); // Setting the panel (and thus the screen) background to white
        add(panel,BorderLayout.CENTER); // Putting panel on the screen (frame)
        panel.setBorder(new LineBorder(BORDER_COLOR,4)); // creating a border for the panel (visual fizzazz)
        
        paintHome(); // paints a page onto the screen cuz haven't done that yet (only made menu so far)
        
        setVisible(true); // makes page visible
    } // end p1main

// All the paint_____ functions are to paint some screen (eg home screen, discussion screen)

    public void paintHome() { 
        // Instruction text
        JLabel ins1 = new JLabel("Discussion Tracker.", JLabel.CENTER); // making text block
        JLabel ins2 = new JLabel("See manual for instructions", JLabel.CENTER); // making text block
        
        // Buttons
        JButton disc = new JButton("Start Discussion");  // to run the class/functions and give output
        disc.setBackground(BUTTON_COLOR); // setting button's color
        disc.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                panel = new JPanel(new GridLayout(1,2,4,4)); // reconfiging the size of the panel to fit the need of the page. Doing it up here so it'll register before the screen refreshes
                panel.setBackground(BACKGROUND_COLOR);
                getContentPane().removeAll(); // basically just a pile of functions that wipes and recompiles screen
                panel.removeAll();
                paintDisc(); // beams to page
                add(panel);
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener

        JButton people = new JButton("Create/Delete People");  // to run the class/functions and give output
        people.setBackground(BUTTON_COLOR); // setting button's color
        people.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                panel = new JPanel(new GridLayout(2, 1,4,4));
                panel.setBackground(BACKGROUND_COLOR);
                getContentPane().removeAll(); // basically just a pile of functions that wipes and recompiles screen
                panel.removeAll();
                paintPeople(); // beams to page
                add(panel);
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener
        
        JButton stats = new JButton("Statistical Summary");  // to run the class/functions and give output
        stats.setBackground(BUTTON_COLOR); // setting button's color
        stats.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                panel = new JPanel(new GridLayout(8, 1,4,4));
                panel.setBackground(BACKGROUND_COLOR);
                getContentPane().removeAll(); // basically just a pile of functions that wipes and recompiles screen
                panel.removeAll();
                paintStats(); // beams to page
                add(panel);
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener
        
        // crams all buttons and text onto screen
        panel.add(ins1); 
        panel.add(ins2);
        panel.add(disc);
        panel.add(people);
        panel.add(stats);
    } // end painthome
    
    public void paintDisc(){ //main page for making new discussions
    
        JPanel classList = new JPanel();
        classList.setLayout(new GridLayout(15,1,4,4)); //makes a panel that each name in the class is put to
        classList.setBackground(BACKGROUND_COLOR);
        JScrollPane classRegion = new JScrollPane(classList); //adds that to a scrollbar on the side
        
        //labels that get filled with the selected person's information
        JLabel personName = new JLabel("(Name)");
        JLabel pronouns = new JLabel("(pronouns)");
        JTextArea notes = new JTextArea();
        
        // Timer Logic
        JButton endDiscussion = new JButton ("End Discussion"); // create button to end all discussions
        endDiscussion.setBackground(BUTTON_COLOR);
        classList.add(endDiscussion);
        endDiscussion.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.out.println("Ending Discussion Timers");
                if (timerRunning == true) {
                    endTime = System.nanoTime();
                    duration = endTime - startTime;
                    System.out.println("Duration:"+duration/1000000000.0);
                }
                startTime = 0;
                endTime = 0;
                timerRunning = false;
                
                
                try (FileWriter discWriter = new FileWriter("Downloads/Discussion.csv",true)) {
                    discWriter.write(personName.getText()+","+pronouns.getText()+","+Double.toString((double)(duration/1000000000.0))+"\n"); 
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
        // end timer logic
        
        //makes buttons out of student names in csv. 
        for (int i = 0; i < datai.size(); i++) { //iterates through datai, which contains downloads/Students.csv
            
            List<String> temp = datai.get(i); //gets the list on each line of datai, in other words the info for one student
            int a = i;
            JButton studenti = new JButton(temp.get(0)); //makes a button with the name obtained from temp
            studenti.setBackground(BUTTON_COLOR);
            classList.add(studenti);// adds button to gui on scroll segment
            //reads each line from the csv and makes a button with that name
           
            //replaces fields with student info from csv. 
            studenti.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Button Pressed"); // some testing stuff
                // --> Timer Block
                if (timerRunning == true) {
                    endTime = System.nanoTime();
                    duration = endTime - startTime;
                    System.out.println("Duration: "+duration/1000000000.0);
                    startTime = System.nanoTime();
                    durations.add((double)(duration/1000000000.0));
                    for (int j=0; j< durations.size(); j++) {
                        System.out.println("Duration: " +durations.get(j));
                    }
                } else {
                    startTime = System.nanoTime();
                    timerRunning = true;
                }
                // end timer block
                
                // this block basically lets you save notes
                try{ // changes the array's value to match the note that was just changed
                    datai.get(b).set(3,notes.getText()); // try because b has a default value of -13 to prevent goofy stuff from happening on the first run through when some stuff hasn't been defined yet
                }catch(Exception exce){
                    System.out.println('e');
                }
                
                // this is to then also change the CSV to match the change in notes
                List<List<String>> tempDatao = new ArrayList<>(); // temp storage for the data that's currently in the CSV
                String lineo; // string to hold lines read from csv
                try(BufferedReader bio = new BufferedReader(new FileReader("Downloads/Students.csv"))) { // read it in
                    while ((lineo = bio.readLine()) != null){ // cram everything including header into 1 big one
                        List<String> rowo = Arrays.asList(lineo.split(",")); // grab 1 row at a time, splitting into indexes by commas
                        tempDatao.add(rowo); // put each row into data blob
                    }
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
                // After grabbing all the data, spit it back in but with the changes necessary to the note
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Downloads/Students.csv"))) { 
                    int q = 0; // to hold which row (basically just the same as having a for loop that's int q = 0; etc.)
                    for (List<String> personData : tempDatao) { 
                        int j = 0; // to hold which column (basically 2d for loop)
                        for (String dataPoint : personData){ // for every datapoint of every person in the csv's data, write it down. If it's not the third/last column, write the data with a comma. If it's the third column, write without a comma. If it's the specific note we want (third column at row b), write in something else
                            if (j != 3){ // if it's not on the last column (3), write a comma along with the data
                                writer.write(dataPoint + ",");
                                j++; // we done looking at the column so add one to that (no need to j++ for the else statement because that's the last column anyways)
                            } else { // otherwise
                                if (q-1 == b){ // if it's the same column as the last discussion person (b), write that one as the notes.getText. q-1 because q contains the header column
                                    writer.write(notes.getText()); // works becuase we haven't rewritten notes yet, so it'll have the value from the last person
                                }else { // otherwise if there's no shennanigans, just write it without a comma because it's the last column and you don't want the code to think there's an extra empty one
                                    writer.write(dataPoint);
                                }
                            }
                        }
                        writer.write("\n"); // after every row, add a newline
                        q++; // we done lookin through the row so add 1 to q to indicate we on row 1 now
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }// end try catch for writing out
                
                //writes order students have spoken to discussion csv.
                try (FileWriter discWriter = new FileWriter("Downloads/Discussion.csv",true)) {
                    if (personName.getText() != "(Name)"){
                        discWriter.write(personName.getText()+","+pronouns.getText()+","+Double.toString((double)(duration/1000000000.0))+"\n"); 
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                }
                
                personName.setText(temp.get(0)); // now that we're done saving the note of the previous person, we can load up the current person's stuff
                pronouns.setText(temp.get(1));
                notes.setText(temp.get(3));
                b = a; // b stores the value of the last button we clicked so setting that to a which is the value of this button (only updating b here so we can run through the last button while on another button)
            }
            });
            

        } 
        

        
        JPanel infoRegion = new JPanel(new GridLayout(8,1,4,4));
        infoRegion.setBackground(BACKGROUND_COLOR);
        
        panel.add(classRegion);
        panel.add(infoRegion);
        infoRegion.add(new JLabel("Name:"));
        infoRegion.add(personName);
        infoRegion.add(new JLabel("Pronouns:"));
        infoRegion.add(pronouns);
        infoRegion.add(new JLabel("Notes:"));
        infoRegion.add(notes);
    }
    
    public void paintPeople(){ // main page for making new people and classes
        JPanel create = new JPanel(new GridLayout(3,3,4,4)); // You know the drill, gridlayout of 3x3 with 4 buffer pixels
        create.setBackground(BACKGROUND_COLOR); // background color
        create.add(new JLabel("Name:", JLabel.CENTER)); // ommitting attaching it to a variable cuz I don't ever need to edit it, just putting a text block into a panel
        create.add(new JLabel("Pronouns:", JLabel.CENTER));
        create.add(new JLabel("Notes:", JLabel.CENTER));
        
        JTextField name = new JTextField(10); // text fields for user to fill in with the person's name, pronouns, and extra notes
        create.add(name); // puttin in panel
        JTextField pronouns = new JTextField(10);
        create.add(pronouns);
        JTextField notes = new JTextField(10);
        create.add(notes);
        
        create.add(new JLabel("Make a", JLabel.CENTER)); // adding instruction text and button to confirm creation (did it down here because gridlayout wants you to add them in specific order)
        JButton confirm = new JButton("Confirm");
        confirm.setBackground(BUTTON_COLOR); // button color
        
        //button to add students to csv. 
        confirm.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ //on button press
                try (FileWriter peopleWriter = new FileWriter("Downloads/Students.csv",true)) {
                    //writes to students, but preserves text that's already there
                    peopleWriter.write(name.getText()+","+pronouns.getText()+","+"0," + notes.getText() +"\n");
                    //writes entered info, plus commas for csv. time defaults to 0 since they shouldn't have any beforehand
                    
                } catch (IOException i) {
                    i.printStackTrace();
                }    
            }
        
        });
        //adds button to gui
        create.add(confirm);
        create.add(new JLabel("student", JLabel.CENTER));
        
        
        // Adding a gridlayout inside a jscrollpane
        JPanel p = new JPanel(); // In essence, you have to add a normal panel, make that into a gridlayout, and then put that panel into the jscrollpane
        p.setLayout(new GridLayout(5, 2,4,4));
        p.setBackground(BACKGROUND_COLOR);
        JLabel deleteInstructions = new JLabel("Press a student to delete them.");
        p.add(deleteInstructions);
        JScrollPane delete = new JScrollPane(p);
        delete.getViewport().setBackground(BACKGROUND_COLOR);
        
        //csv integration
        for (int i = 0; i < datai.size(); i++) {
            List<String> temp = datai.get(i);
            String n2 = temp.get(0);
            JButton studenti = new JButton(n2);
            p.add(studenti);
            //similar to previous code, iterates through datai(downloads/Students.csv) and makes a button for every line in it
            
            studenti.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
                String Search = n2; //name to be deleted
                try {
                    for (List<String> rowi : datai) {
                        String n =rowi.get(0);
                        //basically looks through the csv for a matching name, and deletes it
                        if (Search.equals(n)){
                            //System.out.println(rowi);//to confirm it works
                            
                            datai.remove(rowi); 
                            
                            //basically rewrites the csv from info in datai, which no longer contains the deleted name
                            try (FileWriter peopleWriter = new FileWriter("Downloads/Students.csv")){
                                peopleWriter.write(headeri.get(0)+","+headeri.get(1)+","+headeri.get(2)+","+headeri.get(3)+"\n");
                                for (int j = 0; j < datai.size(); j++) {
                                    peopleWriter.write(datai.get(j).get(0)+","+datai.get(j).get(1)+","+datai.get(j).get(2)+","+datai.get(j).get(3)+"\n");
                                            
                                }
                                        
                            }catch (IOException i) {
                                i.printStackTrace();
                            }
                                    
                        }
                    }//
                } catch (ConcurrentModificationException u){
                    deleteInstructions.setText("the now deceased" + studenti.getText() + " haunts you...");
                }
                
            }
            

            });
        }  
        
        panel.add(create); // adding the 2 scrollpanes to the main panel
        panel.add(delete);
    } // end paintpeople
    
    public void paintStats(){ // main page for discussion and individual stats
         // Instruction text
        JLabel ins1 = new JLabel("Statistics.", JLabel.CENTER); // making text block (page title and subtitle)
        JLabel ins2 = new JLabel("Click on a tab to see details.", JLabel.CENTER); // making text block
        
        // Buttons
        JButton classes = new JButton("Class Stats");  // summaries by class
        classes.setBackground(BUTTON_COLOR); // button color
        classes.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                getContentPane().removeAll(); // basically just a pile of functions that wipes and recompiles screen
                panel.removeAll();
                paintClasses(); // beams to page that organizes data by class
                add(panel);
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener

        JButton demo = new JButton("Demographic Stats");  // summaries by demographic
        demo.setBackground(BUTTON_COLOR); // button color
        demo.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                getContentPane().removeAll(); // basically just a pile of functions that wipes and recompiles screen
                panel.removeAll();
                paintDemo(); // beams to page
                add(panel);
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener
        
        JButton persons = new JButton("Personal Stats");  // summarys by person
        persons.setBackground(BUTTON_COLOR); // button color
        persons.addActionListener(new ActionListener(){ // Checking if button is pressed using actionlistener
            public void actionPerformed(ActionEvent e){
                panel = new JPanel(new GridLayout(1,2,4,4)); // reconfiging the size of the panel to fit the need of the page (2 spots for 2 side by side scrollpanes). Doing it up here so it'll register before the screen refreshes
                panel.setBackground(BACKGROUND_COLOR);
                getContentPane().removeAll(); // basically just a pile of functions that wipes and recompiles screen
                panel.removeAll();
                paintPersons(); // beams to page
                add(panel);
                panel.updateUI(); // redraws it
                revalidate();
                repaint();
            } // end actionperformed
        }); // end actionlistener
        
        // crams all buttons and text onto screen
        panel.add(ins1); 
        panel.add(ins2);
        panel.add(classes);
        panel.add(demo);
        panel.add(persons);
    } // end paintstats
    
    public void paintClasses(){ 
        float sum = 0;
        panel.add(new JLabel("Total time spoken:", JLabel.CENTER));
        for (int i = 0; i< durations.size(); i++)
            sum += 1.0*durations.get(i);
        panel.add(new JLabel(String.valueOf(sum), JLabel.CENTER));
        panel.add(new JLabel("Average time spoken:", JLabel.CENTER));
        if (durations.size ()!= 0){
            panel.add(new JLabel(String.valueOf(sum/(1.0*durations.size())), JLabel.CENTER));
        } else {
            panel.add(new JLabel("Nobody has spoken yet...", JLabel.CENTER));
        }

    }
    
    public void paintDemo(){ // demographic stats
        panel.add(new JLabel(" - DISCONTINUED - ", JLabel.CENTER));
    }
    
    public void paintPersons(){ // stats by person page, GUI stuff 
        JPanel classList = new JPanel();
        classList.setLayout(new GridLayout(15,1,4,4)); // workaround for scrollpane
        classList.setBackground(BACKGROUND_COLOR);
        //makes a panel that each  name in the class is put to
        
        JLabel personName = new JLabel("(Name)"); // filler labels for the 5 datapoints before user clicks on any student
        JLabel avg = new JLabel("(Avg Time Talked)");
        JLabel times = new JLabel("(# of Times Talked)");
        JLabel most = new JLabel("(Talked to the Most)");
        JLabel genderMax = new JLabel("(Gender Talked to the Most)");
        
        for (int i = 0; i < datai.size(); i++) { // for every student
            List<String> temp = datai.get(i); // grab their data
            String n2 = temp.get(0); // grab their name
            JButton studenti = new JButton(n2); // make a button named them

            studenti.addActionListener(new ActionListener(){ // gives the button a trigger
            public void actionPerformed(ActionEvent e){
                personName.setText(n2); // in which it changes the person being displayed to said person
                compilePerson(avg,times,most,genderMax,n2); // and runs the function to find the attached data
            }
            }); 
            classList.add(studenti); // then puts the student button onto panel
        }
        
        JScrollPane classRegion = new JScrollPane(classList); // scrollpane for scrollable list of all students
        //adds that to a scrollbar on the side
        
        
        JPanel infoRegion = new JPanel(new GridLayout(10,1,4,4)); // panel for info
        infoRegion.setBackground(BACKGROUND_COLOR); 


        panel.add(classRegion); // putting the button panel and text info panel on screen
        panel.add(infoRegion);
        infoRegion.add(new JLabel("Name:")); // putting all the stuff into the info panel
        infoRegion.add(personName);
        infoRegion.add(new JLabel("Avg Time Spoken:"));
        infoRegion.add(avg);
        infoRegion.add(new JLabel("# of Times Spoken:"));
        infoRegion.add(times);
        infoRegion.add(new JLabel("Passed to the Most:"));
        infoRegion.add(most);
        infoRegion.add(new JLabel("Gender Passed to the Most:"));
        infoRegion.add(genderMax);
    } // end paintpersons
    
    public void compilePerson(JLabel avg, JLabel times, JLabel most, JLabel genderMax, String name) { // grabs avg time spoken, number of times spoken, person they passed to the most, the gender they passed to the most (grabbing variables to settext() and to grab the name of the person we want)
        String line; // line for file to temp use
        List<String> current; // data from the current person to temp use
        boolean prevWasTarget = false; // basically to be used to check if the current person is going directly after the person we care about (so we can then add them to the data)
        
        double avgTime=0; // stores average time spoken
        int numOfTimes=0; // stores number of times spoken
        HashMap<String, Integer> gendersSpoken = new HashMap<String, Integer>(); // dunno why it's called a hashmap, but basically just dictionary to store number of times a person of a certain name/gender spoke (key being name/gender, value being number of times spoke)
        HashMap<String, Integer> peopleSpoken = new HashMap<String, Integer>(); // wow dictionaries so fancy we didn't learn that i know crazy wild insane *gasp* (python dictionaries are better/more intuitive though)

        try(BufferedReader b = new BufferedReader(new FileReader("Downloads/Discussion.csv"))) { // reads through the list of the order in which people spoke 1 by 1
            while ((line = b.readLine()) != null){ // no header for downloads/discussion.csv so didn't add the code for that. 0th column is name, 1st column is pronouns, 2nd column is time spoken for
                current = Arrays.asList(line.split(",")); // current person we're looking at
                
                if(prevWasTarget) { // if previous person who spoke was the person we want
                    gendersSpoken.put(current.get(1),gendersSpoken.getOrDefault(current.get(1), 0)+1); // add the current person's gender/name to the tally of people our person passed onto
                    peopleSpoken.put(current.get(0),peopleSpoken.getOrDefault(current.get(0), 0)+1);
                    prevWasTarget = false; // change it back to false to be turned to true again when the previous person is our desired person again
                }
                
                if(name.equals(current.get(0))){ // if our current person is equal to the person we want
                    numOfTimes++; // increase the number of timeds theyve spoken
                    avgTime = Double.parseDouble(current.get(2))*1/numOfTimes + avgTime*(numOfTimes-1)/numOfTimes; // weighted average where the current time we're looking at has a weight of 1, and the average of all other times so far have a weight of (number of times - 1 aka num of times so far) to calc average
                    // wow weighted average so fancy (best way to get it all onto one line and not need to divide later and take up more space)
                    prevWasTarget = true; // set to true, as described literally like 6 lines ago
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } // if error, show error
        
        
        String highestGender = "N/A"; // by default, set the person/gender they talked to the most as N/A in case they didn't talk to anyone
        int genderCount = 0;
        for (Map.Entry<String, Integer> entry : gendersSpoken.entrySet()) { // for every key in the genders spoken to dictionary/hash map
            if (entry.getValue() > genderCount) { // check if the times this gender was talked to is higher than our current max
                highestGender = entry.getKey(); // if yes, change to reflect our new max 
                genderCount = entry.getValue();
            }
        }
        
        String highestPerson = "N/A"; // this block literally the same as the one above but with person not gender
        int personCount = 0;  
        for (Map.Entry<String, Integer> entry : peopleSpoken.entrySet()) {
            if (entry.getValue() > personCount) {
                highestPerson = entry.getKey();
                personCount = entry.getValue();
            }
        }
        
        avg.setText(Double.toString(avgTime)); // setting the text blocks/returning data basically
        times.setText(Integer.toString(numOfTimes));
        most.setText(highestPerson);
        genderMax.setText(highestGender);
    } // end compileperson
    
    // Used to compile multiple discussion csvs together, not used. 
    // public static List<String> headerD = new ArrayList<>();  //list composed of strings to store headers for discussions (to then compile data)
    // public static List<List<List<String>>> dataD = new ArrayList<>(); //3d arraylist to store all datapoints from all 2d discussions
    // public void compileData(){ 
    //     String i = "0"; // number to label which discussion is being accessed (eg. discussion1.csv)
    //     boolean looper = true;
    //     while (looper) {
    //         i = Integer.toString(Integer.parseInt(i) + 1);
    //         String line;
    //         try(BufferedReader b = new BufferedReader(new FileReader("Discussion"+i+".csv"))) {
    //             //for header
    //             line=b.readLine();
    //             if (line != null) {
    //                 headerD = Arrays.asList(line.split(","));
    //             }//end header if
    //             //taken from csv lesson
                
    //             List<List<String>> dtemp = new ArrayList<>();
    //             while ((line = b.readLine()) != null){
    //                 List<String> row = Arrays.asList(line.split(",")); // grabs each line in the csv, chops it up
    //                 dtemp.add(row); // puts it in arraylist
    //             }//add all csv to list
    //             dataD.add(dtemp); // puts the discussion's data into the big data pool
    //         } catch(IOException ex) {
    //             looper = false;
    //         }
    //     }
        
    // }
    
    public static void main(String[] args) {
        P1Main main = new P1Main(); // to initialize program
        
  
        String csvi = "Students.csv";
        String linei;
        String spliti = ",";
        
        try(BufferedReader bi = new BufferedReader(new FileReader(csvi))) {
            //for header
            linei=bi.readLine();
            if (linei != null) {
                headeri = Arrays.asList(linei.split(spliti));
            }//end header if
            //taken from csv lesson
            
            //rest of file
            while ((linei = bi.readLine()) != null){
                List<String> rowi = Arrays.asList(linei.split(spliti));
                //makes a string list rowi made from linei(turned into a list and split)
                    //new rowi is made every line, with data from new linei
                datai.add(rowi);
                //adds rowi to datai list, keeping all rowis
            }//add all csv to list
        }catch (IOException e) {
            e.printStackTrace();
        }
        

    } // end main
} 
