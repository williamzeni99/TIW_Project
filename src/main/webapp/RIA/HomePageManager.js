{
    let personalMessage, topicContainer, addForm, logout, pageManager= new pageEditor();

    window.addEventListener("load", () => {
        if (sessionStorage.getItem("username") == null) {
            window.location.href = "LoginJS.html";
        } else {
            pageManager.start();
        } // display initial content
    }, false);

    function RESET(remote){
        if(remote===true){
            pageManager.refresh();
        }
        else{
            pageManager.resetLocally();
        }
    }

    /**Container for a classical Welcome user message*/
    function UserMessage(username, messageContainer){
        this.username=username;
        this.txt="Hi! Nice to see you again";
        this.show=function (){
            messageContainer.textContent= this.txt +" "+ this.username;
        }
    }

    function topicShower(topiccontainerElement){
        this.topicContainer= topiccontainerElement;

        this.show=function (){
            let self=this;
            sendFormData("GET", "../DownloadTopicsJS", null, function (req){
                if (req.readyState == 4 && req.status == 200){
                    let topics=JSON.parse(req.responseText);
                    setDataTopics(topics);
                    if(topics.subtopics.length === 0){
                        self.topicContainer.textContent="No Topics yet"; //todo check this part
                        return;
                    }

                    let ul= document.createElement("ul");
                    self.topicContainer.appendChild(ul);
                    for(let i=0; i<topics.subtopics.length; i++){
                        printer(topics.subtopics[i],ul);
                    }

                    makeDraggable(document.getElementsByClassName("draggable"));
                }
            });

        }

        this.setStoreButton=function (){
            document.getElementById("storeData").addEventListener("click", (e)=>{
                e.stopImmediatePropagation();
                sendJsonObject("POST", "../StoreDataJS", getChanges(), function (req){
                    if(req.readyState==XMLHttpRequest.DONE){
                        let message=req.responseText;

                        switch (req.status) {
                            case 200: //ok
                                let changes=getChanges(); //todo remove
                                RESET(true);
                                window.alert("Your changes were correctly saved.");
                                break;
                            case 400: // bad request
                                RESET(true);
                                document.getElementById("errorTopicMsg").textContent = message;
                                break;
                            case 403: // forbidden
                                RESET(true);
                                document.getElementById("errorTopicMsg").textContent = message;
                                break;
                            case 500: // server error
                                RESET(true);
                                document.getElementById("errorTopicMsg").textContent = message;
                                break;
                            default:
                                break;
                        }
                    }
                });
            });
        }

        function printer (obj, ul) {
            var node = document.createTextNode(obj.id + ". " + obj.name);
            var li= document.createElement("li");
            ul.appendChild(li);
            li.appendChild(node);
            li.setAttribute("class", "draggable");
            li.setAttribute("id", obj.id)

            for (var i = 0; i < obj.subtopics.length; i++) {
                var ul2= document.createElement("ul");
                li.appendChild(ul2);
                printer(obj.subtopics[i], ul2);
            }
        }

        this.RemoteReset=function () {
            this.topicContainer.innerHTML='';
            document.getElementById("errorTopicMsg").textContent="";
            checkStoreDataButton();
            this.show();
        }

        this.LocalReset= function (){
            this.topicContainer.innerHTML='';
            //document.getElementById("errorTopicMsg").textContent="";
            let ul= document.createElement("ul");
            this.topicContainer.appendChild(ul);
            for(let i=0; i<getDataTopics().subtopics.length; i++){
                printer(getDataTopics().subtopics[i],ul);
            }

            makeDraggable(document.getElementsByClassName("draggable"));
            checkStoreDataButton();
        }

        function checkStoreDataButton(){
            let changes=getChanges(); //todo remove
            if(getChanges().length>0){
                document.getElementById("storeData").closest("div").className="normaldiv";
                addForm.hide(true);
            }
            else{
                document.getElementById("storeData").closest("div").className="hide";
                addForm.hide(false);
            }
        }


    } //It works fine

    function addTopicForm(formId){
        let self=this;
        this.formContainer= formId;
        this.selector= document.getElementById("idFather");

        this.addButtonClick= function (){
            document.getElementById("sendButton").addEventListener('click', (e) => {
                e.preventDefault();
                e.stopImmediatePropagation();

                if(getChanges().length>0){
                    window.alert("Permission dined! You have unsaved changes. Save it before adding new topics.");
                }
                else{
                    let form = self.formContainer;
                    if (form.checkValidity()) {
                        sendFormData("POST", '../AddTopicJS', self.formContainer,
                            function (x) {
                                if (x.readyState == XMLHttpRequest.DONE) {
                                    let message = x.responseText;
                                    switch (x.status) {
                                        case 200: //ok
                                            RESET(true);
                                            break;
                                        case 400: // bad request
                                            document.getElementById("errorMsg").textContent = message;
                                            break;
                                        case 401: // unauthorized
                                            document.getElementById("errorMsg").textContent = message;
                                            break;
                                        case 403: //not found
                                            document.getElementById("errorMsg").textContent = message;
                                            break;
                                        case 500: // server error
                                            document.getElementById("errorMsg").textContent = message;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        );
                    }
                    else {
                        form.reportValidity();
                    }
                }
            });
        }

        this.Reset= function (remote){
            document.getElementById("errorMsg").textContent="";
            document.getElementById("idFather").innerHTML='';
            document.getElementById("topicName").textContent="";
            if(remote===true){
                RemoteFillOption();
            }
        }

        function RemoteFillOption (){
            sendFormData("GET", "../GetOptionsTopicJS", self.formContainer, function (req){
                if(req.readyState==XMLHttpRequest.DONE && req.status==200){
                    let ids=JSON.parse(req.responseText);

                    for (let i=0; i<ids.length; i++){
                        let option= document.createElement("option");
                        option.text=ids[i];
                        option.value=ids[i];
                        self.selector.appendChild(option); //todo check this part
                    }
                }

            }, false);
        }

        this.hide= function (bool){
            if(bool===true){
                self.formContainer.closest("div").className="hide";
            }
            else {
                self.formContainer.closest("div").className="";
            }
        }


    }

    function logoutAction(button){
        this.button= button

        this.addButton= function (){
            this.button.addEventListener("click", (e)=>{
                sessionStorage.clear();
                e.target.closest("form").submit(function (){return true});
            });
        }
    }

    function pageEditor(){
        this.start=function (){
            resetChanges();
            personalMessage = new UserMessage(sessionStorage.getItem("username"), document.getElementById("username"));
            personalMessage.show();

            topicContainer= new topicShower(document.getElementById("topics"));
            topicContainer.show();
            topicContainer.setStoreButton();

            addForm=new addTopicForm(document.getElementById("formAdd"));
            addForm.Reset(true);
            addForm.addButtonClick();

            logout= new logoutAction(document.getElementById("logoutButton"));
            logout.addButton();
        }

        this.refresh=function (){
            resetChanges();
            addForm.Reset(true);
            topicContainer.RemoteReset();
        }

        this.resetLocally=function (){
            topicContainer.LocalReset();
            addForm.Reset(false);
        }

    }

}