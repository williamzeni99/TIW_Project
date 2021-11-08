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
            checkStoreDataButton();
            this.show();
        }

        this.LocalReset= function (){
            this.topicContainer.innerHTML='';
            let ul= document.createElement("ul");
            this.topicContainer.appendChild(ul);
            for(let i=0; i<getDataTopics().subtopics.length; i++){
                printer(getDataTopics().subtopics[i],ul);
            }

            makeDraggable(document.getElementsByClassName("draggable"));
            checkStoreDataButton();

        }

        function checkStoreDataButton(){
            if(getChanges().length>0){
                document.getElementById("storeData").closest("div").className="normaldiv";
                document.getElementById("storeData").addEventListener("click", (e)=>{
                    e.stopImmediatePropagation();
                    sendJsonObject("POST", "../StoreData", getChanges(), function (req){
                        if(req.readyState==XMLHttpRequest.DONE){
                            let message=req.responseText;

                            switch (req.status) {
                                case 200: //ok
                                    RESET(true);
                                    window.alert("Your changes were correctly saved.");
                                    break;
                                case 400: // bad request
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

    function addTopicForm(formId){
        let self=this;
        this.formContainer= formId;
        this.selector= document.getElementById("idFather");

        this.addButtonClick= function (){
            document.getElementById("sendButton").addEventListener('click', (e) => {
                e.preventDefault();
                e.stopImmediatePropagation();

                if(getChanges().length>0){
                    if(!confirm("You have changes unsaved. Do you want to proceed anyway? This changes will be lost.")){
                        return;
                    }
                }

                var form = self.formContainer;
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
            });
        }

        this.reset= function (remote){
            document.getElementById("errorMsg").textContent="";
            document.getElementById("idFather").innerHTML='';
            document.getElementById("topicName").textContent="";
            if(remote===true){
                RemoteFillOption();
            }
            else{
                LocalFillOption();
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

        function LocalFillOption(){
            let ids=new Array();
            getSons(getDataTopics(), ids);

            for (let i=0; i<ids.length; i++){
                let option= document.createElement("option");
                option.text=ids[i];
                option.value=ids[i];
                self.selector.appendChild(option); //todo check this part
            }
        }

        this.hide= function (bool){
            if(bool===true){
                self.formContainer.className="hide";
            }
            else {
                self.formContainer.className="";
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
            personalMessage = new UserMessage(sessionStorage.getItem("username"), document.getElementById("username"));
            personalMessage.show();

            topicContainer= new topicShower(document.getElementById("topics"));
            topicContainer.show();

            addForm=new addTopicForm(document.getElementById("formAdd"));
            addForm.reset(true);
            addForm.addButtonClick();

            logout= new logoutAction(document.getElementById("logoutButton"));
            logout.addButton();
        }

        this.refresh=function (){
            addForm.reset(true);
            topicContainer.RemoteReset();
            resetChanges();
        }

        this.resetLocally=function (){
            topicContainer.LocalReset();
            addForm.reset(false);
        }

    }

}