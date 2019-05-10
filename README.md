# Student-management-system
My first year JAVA course design.

## Ⅰ System models and architectures  
In my JAVA course design experiment of student management system, I adopted MVC architecture while using C/S architecture.  
MVC, which full name is Model View Controller, is an abbreviation for Model, View and Controller. The Model is responsible for data exchange and function implementation, the View is responsible for interaction with the user, just called Ul (User interface), and the Controller is used to ensure the synchronization of the Model and View, and make the display of the View correspond to the Model when it changes.  
The advantage of the MVC architecture is that it improves and personalizes the interface and user interaction without having to rewrite the model part. The modular design enables close coordination between functions, and functional modules can be removed for easy maintenance.  
The disadvantage of the MVC architecture is that it is too cumbersome to develop an architecture for a small or medium-sized project. Execution is slightly less efficient.  
So, why did I choose MVC architecture?    
First of all, our course design experiment requires a lot of function. Private chat, group chat, notice, perusal, file transfer, etc., need a lot of interface and model. Complexity has reached the standard for applying MVC architecture, in which case only MVC architecture can easily control these functional modules.  
Secondly, I would like to take this course design experiment as a challenge to myself, which is also my first attempt to use MVC architecture to write medium-sized applications. I hope this course design can further improve my programmer skills.  
In the end, I firmly believe that my hard-working can exchange for the top performance ranking!    
### 1.1	My research of MVC architecture
My first facing for this architecture is in the open source code for the RPG Maker MV game engine. And on the end of last year, I have studied of the Unreal Engine 4 source code, then found that it has a similar architecture of MVC. So, I began to think about the rationality of this program structure.  
MVC, as a kind of modular architecture, is based on object-oriented programming. Each set of functional objects is treated as a module. Most of these modules belong to the Model (logic module) and View (View module, usually the user interface). The Controller is responsible for synchronizing them so that when the Model changes, the View is updated synchronously to form user interaction.  
It should be notice that both Model and View can be uninstalled. If you uninstall some part of the program, you can still run is while losing some functions. I found similar using of that in RPG Maker MV and Unreal Engine 4. They all define each view as a scene that can be switched through the scene manager, and removing one does not affect the other.  
The authority mechanism of MVC architecture is also special. To operate between modules of the same level, it is necessary to pass to higher-level modules, which judge and distribute commands. For example, in my program, a window that wants to close itself can’t call its dispose function directly. You must upload the shutdown request to the SceneController, and the controller can filtrate it then determine whether it can be closed or not. In case of if it is closed, will it conflict with other Windows. If there is no problem, then the controller sends the shutdown callback to this window. If the SceneController can’t judge the window can or can’t close, for example, if the window is holding by a low-priority thread, the SceneController has to pass the request to the ThreadController, which determines if the thread interruption will cause a problem, and then issue the command. Passing commands layer by layer and distributing callbacks layer by layer not only ensures security, but also increases program stability.  
### 1.2	The main model of my program
(Client only): Only use in client.  
(Server only): Only use in server.  
(Client/Server) Using on client and server. Both of them have two independent parts.  
#### 1.2.1 Controllers
    (Client/Server) ThreadController: multithreading controller of the thread pool, storages the link of all the program threads. It can allocate thread priority, solve thread conflicts, optimize the execution efficiency, prevent some of deadlocks. In addition, the Client also manages the update coroutine of the client user interface.  
    (Client/Server) ConnecterController: Network connection controller. It is the communication controller of server and client, encapsulated socket functions. Communication between the server and the client can only be achieved through the ConnectorController. After the ConnecterController receives the communication content, it distributes them by the token.   
    (Client Only) SceneController：Scene controller. I think of each interface as a scene card (Scene), which is used to manage the scene tabs in a window, ensuring that no Scene is opened twice at the same time. It is also possible to implement sequential exchange of Scenes in the window.
    (Client Only) WindowController: Multi-window controller. Manage multiple windows, mainly responsible for opening child windows. It cooperates with the SceneController, which can ensure that there is no repeated Scene between the windows, automatically give the focus to the selected Scene, and can also implement the Scene drag between the windows.
    (Client Only) AdjusterController: Animation controller. Manage updates for Adjuster (see 2.2.5).
    (Client Only) GraphicsController: Graphics2D and color controller. Stores the system color and brush settings required for the drawing.
    (Client Only) ClientController: Client controller. Responsible for the startup of client, Hand over permissions to other controllers after startup. 
    (Client Only) MusicController: Controller of music, used to play musics.  
    (Server Only) ServerController: Server controller. Responsible for the startup of server. Hand over permissions to other controllers after startup.
    (Server Only) ConsoleController: Console command controller. Enables the server administrator to operate the server by code at the console.
    (Server Only) UserController: User Manager. Responsible for administrator authority check, user login status judgment and command authority determination. Used to handle user operations, as well as provide mappings between users and Connecters.
#### 1.2.2 Models:
    (Client/Server) CommandModel: Command module. Responsible for receiving the command, parsing and distributing. CommandController provides a mapping table from token to code, and you can add commands as long as you add a new map. Used to maintain synchronization between the server and the client.
    (Client Only) LanguageModel: Multi-language module. Defines the system keywords, is responsible for reading the language library, for distributing the keywords in client, and implement dynamic language switching.
    (Client Only) StorageModel: Storage module. Responsible for accessing the client's settings and message records.
    (Client Only) ImageModel: Image module. Store image caches to save time when reading images repeatedly.
    (Server Only) LogModel: Log module. Responsible for outputting server logs, including regular logs and administrator action logs.
    (Server Only) MySQLModel: Database module. Responsible for interacting with the MySQL database to access data from it.
### 1.3	The awesome features based on the MVC architecture
In this course design experiment, I not only completed the required functions, but also added some of interesting features. Below I will show these features, giving screenshots and key code (attached to the end of the technical report).  
All interfaces are implemented using related classes in javax.swing. I encapsulate a lot of components that extends from the swing package, and override the paintComponent function to make the interface more beautiful.  
#### 1.3.1 Basic functions
##### Notice of class
##### Functions: send notice, delete notice (Administrator privileges), send photos, the simple layout of the document, import HTML document.
Users are not allowed to register and choose classes by themselves, Only the database administrator can import username and class info. This is also the characteristics of the class management system.  
You can enter the class chat or view the list of members by the Class Interface.  
Click the expand button of the notice list to expand the notice, then click it again to close. The notice list displays information such as the title of the notice, who have sent it, and when.  
In the notice list, if you are login as an administrator, the delete button is acquired. The button is only the interface, and there is another validation on the server side, that is, if it’s not the administrator login, and the delete button displayed by malicious modifying the client, then it’s useless to click. (That is same for the sensitive operations below, such as deleting files and perusals, or withdraw chats).  
Administrators can also release notices to classmates.  
The notice editor consists of a title area, a toolbar, and a text field. You can import files, insert pictures, undo, redo, set bold, italic, underline, delete line, superscript, subscript, change font face, font size, text color, and set four paragraph alignments (left alignment, center, right alignment, and justified).  
When an image is being inserted, if the width of the image is larger than the width of the text field can be displayed, then it automatically scales to the appropriate width.  
Import files allows you to import existing HTML documents. You can edit your notice in Microsoft Word, save it as an Html file, and then import to my notice editor.  
##### Administrator sensitive operation log
Log model can detect sensitive operations, then generate a log file on the server side that logs the actions of the administrator. The permission administrators can only be imported in the background by the database administrator.  
In particular, only sensitive operations performed by administrators, that means not the operations can ordinary members do (such as releasing or deleting notices, deleting other people's files instead of own) will be recorded.  
##### Document perusal and vote with anonymity
##### Functions: send perusal, delete perusal, send photos, the simple layout of the document, import HTML document, anonymous comment, anonymous vote.
The interface of perusal is similar to the notice interface.  
Click the expand button to expand the circulated content. From top to bottom are the document content, voting area, and comment area.  
In the voting area, all options are displayed, along with the maximum number of choices allowed. It can be selected by clicking on the option button. If it is a single choice, other options will be automatically canceled when selecting. About multi-selected, the option can't be selected if the number you have selected reach the maximum. You must cancel at least one selected option to continue the selection.  
Click release button to release your perusal. After the release, you will be able to see everyone's choices.  
In the comment area, the text field is displayed. Comment has a limit of 150 words and can be sent by clicking release button. Anonymous comments will be displayed at the bottom of the perusal. The same user can comment multiple times.  
When deleting a perusal, the publisher can delete the perusal that he or she has posted, the administrator can delete all the perusal.  
Any class member can post a perusal. The Perusal Editor is similar to the Notice Editor with the option window added. You can add new options by clicking the add option button.  
Click the increase/decrease button to set the maximum number of choices, 1 for single selection. In particular, this maximum number cannot exceed the number of options in the options list (at least 1). If the delete option causes maximum number to be larger than the number of options, maximum number will be automatically reduced.  
Click the move down button on the right of the option to move option down, or click delete button to delete it.  
##### File sharing (upload, download)
##### Functions: upload files, download files, delete files, MD5 compare.
The file sharing interface allows you to view a list of files and upload/download files with your classmates.  
Click send button, a selection box will pop up, you can choose and send the file.  
Before sending the file, client calculates the MD5 value of the file and uploads it to the server, compare to the server files. If the file already exists (not limited in this class), you can upload the file in seconds. A map is generated in the database.  
During the upload process, real-time progress and speed will be displayed.  
Files that have already been uploaded can be downloaded and deleted.  
The selection box will also pop up when downloading, and then similar to the upload, showing the real-time progress and speed.  
Similar to perusal, users can delete files they upload, and administrators can delete all files. In particular, if there are multiple mappings for a file, only the corresponding mapping will be deleted. The file itself is deleted only if the mapping for the file is the remaining one.  
##### Instant messaging (multiple)
##### Functions: send message, chat bubble, withdraw message, send pictures and simple layout of documents.  
Through the class interface, you can enter group chat.  
The chatroom interface displays the username, chat bubble, and timestamp.  
Chat text editing can also implement simple format operations. When you add an image, it is automatically scaled to the appropriate size.  
If the message is withdrawn, it won’t be shown. Users can withdraw their own messages and the administrator can withdraw all messages.  
##### Instant messaging (private)
Through the member list, you can enter chatroom and have private chats with class members.  
If the message is withdrawn, it won’t be shown. Users can withdraw their own messages.  
##### Child windows
Functions: multi-window, sequential exchange, drag scene across windows.  
Based on WindowController, I implemented a multi-window operation. You can open a child window by clicking the multi-window button on the main interface.  
Users can drag scene tabs, change the order of tabs in the window, and move tabs to other windows.  
There is no limit to the number of child windows that can be popped up.  
#### 1.3.2 Additional funtions
##### Instant hand drawing: Paint and Guess
##### Functions: tablet support, instant drawing, simple vector lines.
Enter the minigame interface, you can view Paint and Guess.  
Enter the game room list, click on the existing room to enter, or click on the new room to create a gameroom.  
After the room is full, the game starts automatically.  
The drawing side supports smoothing display, undo, clear screen, redo, recovery, stroke thickness, color and other functions. My drawing program supports digital device input and automatically recognizes pen pressure.  
The receiver will get three hints and answers respectively for the remaining 55 seconds, 50 seconds, 10 seconds and 5 seconds.  
##### Multi-language switching function
##### Functions: language switching, import language package.
College students come from around the world. In particular, our Shandong University, as a world-famous college, must have foreign exchange students. And Taiwan Province of China is also accustomed to using traditional Chinese. As a mature class management system, how can there be no language switching function?  
With the convenience of the MVC architecture, I made a language switching system. The language library storages on client, with the .ini file type, can be freely selected in the option interface. I wrote Simplified Chinese, Traditional Chinese, English and Japanese.  
What if friends from other countries want to use it? You can click Export Blank Language Pack to generate a blank language pack, fill up the banks, create more language libraries and share them with everyone!  
Of course, students who like to be funny can also customize the client through the language library, for example, change the send button to "touch". Is it very interesting~?  
##### Music player
##### Functions: play, pause, real-time spectrum display
Because I like listening music when I do my program, I really want to do some cool things, So I designed a music player.
Click the play button to select wav format music for playing.  
The interface will display the spectrum, playback progress, lyrics and song name in real time, and the title bar will have a colorful effect. There are also some other special effects. For the display of the spectrum, see 2.2.7.  
Click the pause button to pause music.  
Click the stop button to stop music.  
## Ⅱ Technical difficulties
### 2.1 Technological difficulties in architecture
This section will explain the problems and solutions I faced when using MVC and C/S architecture.  
#### 2.1.1 Client view architecture
The core of the client view is Scene and Window, which is described in the previous system module architecture.  
##### When creating a new scene:
Step 1: The button submits a new scene request to the SceneController.  
Step 2: SceneController submits the request to WindowController.  
Step 3: WindowController traversals all the SceneControllers, and asks if the Scene already exists. If it exists, refuse to create a scene, and send a command to the SceneController where the target scene is located, send focus to the existing scene; if it does not exist, approve the request of scene creating, send a new scene command to the SceneManager, and let the Window ready to accept the scene loading.  
##### When moving the scene:
Step 1: The button submits an exchange scene request to the SceneController.  
Step 2: SceneController submits the request to WindowController.  
The third step: WindowController detects whether the mouse release position belongs to a Window. If it is outside the interface, the request is refused: if it is inside a Window, send the exchange command to the SceneController of the Window, and the SceneController be ready to accept the scene loading.  
Step 4: The SceneController receives the command. If it finds that the command is sent by itself, it changes the sequence of the scene, and the operation ends. If it finds that the command is from another scene, it sends a callback to the WindowController.
Step 5: WindowController accepts the callback parameters and synchronizes the Scene state.  
All views are processed by coroutines to ensure that all views are in the same state.  
#### 2.1.2 Generate virtual servers to ease the pressure of game rooms and file transfer performance.  
In my course design experiment – Paint and Guess, you need to use the function of opening the game room. The game has fast data transmission, a large amount of data packages, and high concurrency. It is possible to transmit dozens of data per second to each client in the game room. That may cause high concurrency conflicts to affect stability. In order to solve the performance problem, the game room server must be separated from the main server.  
In addition, file transfer is also a big problem. When a user transfers a relatively large file, it will cause the main connection be blocked, which greatly affects server performance.  
Therefore, I designed a variety of servers: login authentication / chat server, file server and game room server. Since my project is not very big, I have launch all three servers in my computer.  
I have learned about the server architecture of the King of Glory game, "King of Glory" uses Proxy routing to achieve dynamic resource allocation. PVP servers and room servers are dynamically allocated. Its large distributed physical servers can keep running even if one or two of server breakdown. In my course design, Server_Main acts as both a proxy routing and chat service, and Server_File is dynamically scheduled by Server_Main as a file transfer server (see 2.2.1). Server_PaintAndGuess is dynamically scheduled by Server_Main too.  
When the client sends a new game room request to server, my Server_Main route will automatically partition an available resource and launch a virtual game room server, which will be controlled by the main server to ensure the game process and avoid high concurrent conflicts with other servers. This is a simple application of distributed thinking can solve performance problems. Because I divide an area on my computer for a new server instead of deploy it to other physical servers, it is a virtual server.  
Based on the control of the MVC architecture, I made a very simple, incomplete architecture with virtual server ideas. But this simple and incomplete architecture also makes my course design more complete. Include the MD5 verification function about the file and the dynamic allocation of the game room, all based on this idea.  
### 2.2 Technological difficulties in function
This section will explain the difficulties I faced while making features.  
#### 2.2.1 MD5 verification and concurrent processing of file transfer
In 2.1.2, I mentioned that I use separate servers do file transfer. However, file transfer has many concurrency issues at the same time. such as:  
When a user deleting a file, others may download it.  
If the same file shared in multiple classes, such as JAVA learning material, a file has size of 1GB, storing file in each class is a waste of server resources. You may say that I can put all the files together, but then there is a new problem: if you put them together directly, the files with the same name will overwrite others, and conflicts will happen when you delete them. It is possible that after a class delete the file, other classes can’t use it.  
If there is already have a same file on the server, then let the user re-upload the 1GB JAVA learning material, is a waste of network flow. Although I can get money with network flow, the user experience is obviously more important.  
If two people upload a file with the same name at the same time, it is likely to cause concurrent writing, then the resulting file will breakdown.  
How to solve that? File transfer function is obviously not that simple.  
To this end, I used the file buffer pool and MD5 verification.  
##### When uploading files:
Step 1: Client calculates the MD5 value of the file and upload request to the Server_Main routing server.  
Step 2: The Server_Main routing server receives the request, then compare to the database. If the MD5 value of the file exists in the database, just write a record to the existing file without upload a new one, then the operation ends; if the data of this file is not found in the database, the server_File will prepare the connection, then return a command to the client.  
Step 3: The client sends a Socket connection to Server_File. This step must after the success of previous step. If the connection fails (the client is modified, the permission verification fails, the data for the file with same name already exists in the buffer pool, etc.), the request is refused, and the operation ends; if the connection is successful, the unique identification ID will be sent to the Server_Main routing server, ready to send file.  
Step 4: The Server_Main routing server receives the identification ID, passes the ID to Server_File, put the connection to buffer pool, and starts file transfer.  
Step 5: After the file transfer succeeds, Server_File pushes the buffer pool resource into storage space, clears the buffer pool, generates a mapping from MD5 to the file name, and then push into database.  
##### When downloading files:
Step 1: Client uploads the download request to the Server_Main routing server.  
Step 2: The Server_Main routing server receives the request, then compare to the database. If the mapping of this file doesn’t exist in the database (for example, the file is just being deleted when the client makes a download request), the operation ends; if the data exists, the server_File will prepare the connection, then return a command to the client.  
Step 3: The client sends a Socket connection to Server_File. This step must after the success of previous step. If the connection fails (the client is modified, the permission verification fails, etc.), the request is refused and the operation ends; if the connection is successful, the unique identification ID will be sent to the Server_Main routing server, ready to receive the file.  
Step 4: The Server_Main routing server receives the identification ID, passes the ID to Server_File, put the connection to the buffer pool, and starts file transfer.  
Step 5: After the file transfer is successful, Server_File clears buffer pool data.  
##### When deleting a file:
Step 1: Client uploads the delete request to the Server_Main routing server.  
Step 2: The Server_Main routing server receives the request, determines the permission first. If there are multiple mappings of target file in the database, only the records are deleted; if the target mapping of the file is unique, the records are deleted first, and then Server_File prepare to delete the file.  
Step 4: Server_File receives the request, waiting for all the downloads of target file complete, then delete the file.  
Due to the time limit of course design, pause function is not currently supported.  
#### 2.2.2 The algorithm of vector line for Paint and Guess  
Since the mouseDragged event in the MouseListener has a response interval, when drawing a picture quickly, you can only have many dots instead of a line.  
There are two solutions to this problem:  
The first one: use another thread to capture the mouse position, but this will cause a huge resource occupation. And in this case, you need to transfer image when uploading drawing data. That will cause a large use of network flow.  
The second one: Use a two-dimensional linked list, each element is a line, the coordinates of each point are recorded in the line. When drawing, connects the points one by one to draw lines. Only coordinate data is transmitted to server. This is very resource-saving, but if the mouse moving fast, the space between two points will be too far, it will become a polyline, especially when drawing a circle very fast, it will be drawn as a polygon; If I take point with short interval, the cost of network flow will greatly increase.  
I used the second solution, but I also design a vector line algorithm that perfectly solves the problem of polyline!  
The vector line of photoshop, each point has two arms, as well as length, angle and other attributes. I have no ability to study such a complicated algorithm, so I designed my one-arm vector line, each with five attributes: XY coordinates, brush radius, arm angle and color. The arm length is half of the spacing of two points, and the point radius weights are evenly distributed. The result of this calculation has a gap between the real line, but it is undoubtedly the most efficient and most useful for my project.  
After applying the algorithm, compare to send image, the transmission efficiency increased by 3 to 10 times. Compare to the polyline, the transmission efficiency reduced by 40%. It can be said that I used extra 40% network flow for 6 times improvement.   
#### 2.2.3 The canvas with pen pressure from the digital input device  
In fact, I have a hobby of painting cartoon characters. I have a professional drawing device - a digital screen. It has pressure sensing and can change the thickness of the stroke according to the pen pressure.  
So, since I have device, why not let my course design support digital input?  
I use the JPen open source library. It can use dll to identify pen info. If it is detected that it is currently a digital input, I will add the pen pressure to the point algorithm (associated with the point radius and XY coordinates); if it is a mouse input, it will change the point radius by default. This achieves a line with pressure sensing!  
Personal testing, it can’t compare with professional painting software, such as Photoshop, but slightly better than Windows Ink.
#### 2.2.4 Multi-language switching  
I must use setText function to change the text of buttons or labels in JAVA swing. This means I must get references to all the elements. Obviously, this is troublesome. Is there any other way to achieve this?  
I created a Controllable interface with update and refresh methods for coroutines and initiative updates. My buttons and labels implement the Controllable interface. In each button's refresh method, it gets the latest text and repaint.  
I think of text as a resource in the MVC architecture. First, I created a Language class. The Language object contains a HashMap with a correspondence between the key value and the text. It is a database object. After that, the LanguageModel interacts with the Language. When switching languages, you only need to switch the target Language object and then call the refresh method in WindowController to do language switching.  
Each button and JLabel has a textKey that corresponds to the key value in the Language object. After the update method is called,   LanguageModel looks for the value in the Language object based on the key, Then the requester updates its text.  
In this way, the dynamic switching of the text is completed without get the reference to the element.   
#### 2.2.5 Animations
The buttons and text boxes need to be animated, having different feedback when the mouse moving over the button and press down. Text boxes are more difficult because the default text box of javax.swing has no hint function, such as "Please enter a username."  
The update method in the Controllable interface is prepared for this. The gradation or hint are completed by an animation update of 30 frames per second. In addition, I also wrote the Adjuster class. Each time the update method runs, the Adjuster will update once, and the animation values will change according to the method. You can use the AdjusterController to invert, pause, reset the animation values in the Adjuster to achieve special animation effects.  
The XOR triangle animation on the login interface is using Adjuster. Same as buttons and text boxes.  
#### 2.2.6 Text editor
My notice editor uses HTMLEditorKit in javax.swing.text package.  
I realized it through the modification of the style Attribute, these functions are all encapsulated in HTMLEditorKit.  
The text editor seems to be the most complicated function, but because it uses HTMLEditorKit, it is mentioned at the end.  
#### 2.2.7 Audio Spectrum Display
Instant spectrum display requires the application of a fast Fourier transform.  
First, using a window function on the WAV waveform to get a sampling, and then apply fast Fourier transform to obtain a set of discrete data, which is the spectrum of the signal. after some calculate, it can be displayed on the interface and become the spectrum map that everyone sees.  
## Imported JAVA library:  
commons-codec-1.12.jar http://commons.apache.org/proper/commons-codec/  
LICENSE: Apache License 2.0  
For Base64 and MD5 decode.  
commons-math3-3.6.1.har http://commons.apache.org/proper/commons-math/  
LICENSE: Apache License 2.0  
For Fast Fourier Transform Operation.  
gson-2.8.5.jar https://github.com/google/gson  
LICENSE: Apache License 2.0  
For send json between server and client  
jpen-2.jar (jpen-2-3-64.dll) https://sourceforge.net/projects/jpen/  
LICENSE: GNU Library or Lesser General Public License version 2.0 (LGPLv2)  
For getting pen pressure with digital tablet.  
mysql-connector-java-8.0.15.jar https://www.mysql.com/  
LICENSE: https://www.oracle.com/legal/terms.html  
For connecting MySQLdatabase.  
## References
Bruce Eckel〔M〕Thinking in JAVA, 机械工业出版社, 2007-06-01  
JoshuaMarinacci/ChrisAdamson〔M〕Swing Hacks, O'ReillyMedia, 2005-06-30  
蜘蛛侠不会飞〔EB/OL〕使用python进行傅里叶变换FFT绘制频谱图,  https://blog.csdn.net/qq_40587575/article/details/83316980  
