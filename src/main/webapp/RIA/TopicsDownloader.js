{
    let personalMessage, topicContainer, addForm, logout, pageeditor= new pageEditor();

    let modified=[];
    let dataTopics;



    window.addEventListener("load", () => {
        if (sessionStorage.getItem("username") == null) {
            window.location.href = "LoginJS.html";
        } else {
            pageeditor.start(); // initialize the components
        } // display initial content
    }, false);

    function UserMessage(username, messageContainer){
        this.username=username;
        this.show=function (){
            messageContainer.textContent= this.username
        }
    }

    function StoreData(data){
        modified.push(data);
    }

    function getDataTopics(){
        return dataTopics;
    }
    function setDataTopics(newData){
        dataTopics=newData;
    }


    function topicShower(topiccontainerElement){
        this.topicContainer= topiccontainerElement;

        this.show=function (){
            var self= this;

            sendFormData("GET", "../DownloadTopicsJS", null, function (req){
                if (req.readyState == 4 && req.status == 200){
                    var topicstoshow=JSON.parse(req.responseText);
                    setDataTopics(topicstoshow);
                    if(topicstoshow.subtopics.length === 0){
                        self.topicContainer.textContent="No Topics yet";
                        return;
                    }

                    var ul= document.createElement("ul");
                    self.topicContainer.appendChild(ul);
                    for(var i=0; i<topicstoshow.subtopics.length; i++){
                        printer(topicstoshow.subtopics[i],ul);
                    }

                    makeDraggable(document.getElementsByClassName("draggable"));
                }
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

        this.resetServerAsking=function () {
            this.topicContainer.innerHTML='';
            modified=[];
            checkStoreDataButton();
            this.show();
        }

        this.resetLocally= function (){
            this.topicContainer.innerHTML='';
            var ul= document.createElement("ul");
            this.topicContainer.appendChild(ul);
            for(var i=0; i<dataTopics.subtopics.length; i++){
                printer(dataTopics.subtopics[i],ul);
            }

            makeDraggable(document.getElementsByClassName("draggable"));
            checkStoreDataButton();

        }

        function checkStoreDataButton(){
            if(modified.length>0){
                document.getElementById("storeData").closest("div").className="normaldiv";
                document.getElementById("storeData").addEventListener("click", (e)=>{
                    e.stopImmediatePropagation();
                    sendJsonObject("POST", "../StoreData", modified, function (req){
                        if(req.readyState==XMLHttpRequest.DONE){
                            var message=req.responseText;

                            switch (req.status) {
                                case 200: //ok
                                    pageeditor.refresh();
                                    window.alert("Your changes were correctly saved.");
                                    break;
                                case 400: // bad request
                                    document.getElementById("errorStoreMsg").textContent = message;
                                    break;
                                case 401: // unauthorized
                                    document.getElementById("errorStoreMsg").textContent = message;
                                    break;
                                case 403: //not found
                                    document.getElementById("errorStoreMsg").textContent = message;
                                    break;
                                case 500: // server error
                                    document.getElementById("errorStoreMsg").textContent = message;
                                    break;
                                default:
                                    break;
                            }
                        }

                    });
                });
            }
            else{
                document.getElementById("storeData").closest("div").className="hide";
            }
        }


    } //It works fine

    function addTopicForm(formId, pageEditor){
        var self=this;
        this.formContainer= formId;
        this.pageEditor= pageEditor;
        this.selector= document.getElementById("idFather");
        this.fillOption= function (){
            sendFormData("GET", "../GetOptionsTopicJS", self.formContainer, function (req){
                if(req.readyState==XMLHttpRequest.DONE && req.status==200){
                    var ids=JSON.parse(req.responseText);
                    var option= document.createElement("option");
                    option.text="/";
                    option.value="0";
                    self.selector.appendChild(option);
                    for (let i=0; i<ids.length; i++){
                        option= document.createElement("option");
                        option.text=ids[i];
                        option.value=ids[i];
                        self.selector.appendChild(option);
                    }
                }

            }, false);
        }

        this.addButtonClick= function (){
            document.getElementById("sendButton").addEventListener('click', (e) => {
                e.preventDefault();
                e.stopImmediatePropagation();

                if(modified.length>0){
                    if(!confirm("You have changes unsaved. Do you want to proceed anyway? This changes will be lost.")){
                        return;
                    }
                }

                var form = self.formContainer;
                if (form.checkValidity()) {
                    sendFormData("POST", '../AddTopicJS', self.formContainer,
                        function (x) {
                            if (x.readyState == XMLHttpRequest.DONE) {
                                var message = x.responseText;
                                switch (x.status) {
                                    case 200: //ok
                                        self.pageEditor.refresh();
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
            });
        }

        this.reset= function (){
            document.getElementById("errorMsg").textContent="";
            document.getElementById("idFather").innerHTML='';
            this.fillOption();
            document.getElementById("topicName").textContent="";
        }

    }

    function logoutAction(){

        this.addButton= function (){
            document.getElementById("logoutButton").addEventListener("click", (e)=>{
                sessionStorage.clear();
                e.target.closest("form").submit(function (){return true});
            });
        }
    }

    function pageEditor(){
        this.start=function (){
            personalMessage = new UserMessage(sessionStorage.getItem("username"), document.getElementById("username"));
            personalMessage.show();

            topicContainer= new topicShower(document.getElementById("topics"));
            topicContainer.show();

            addForm=new addTopicForm(document.getElementById("formAdd"), this);
            addForm.fillOption();
            addForm.addButtonClick();

            logout= new logoutAction();
            logout.addButton();
        }

        this.refresh=function (){
            addForm.reset();
            topicContainer.resetServerAsking();
        }

    }

};