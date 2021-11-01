{
    let personalMessage, topicContainer, addForm, pageeditor= new pageEditor();

    window.addEventListener("load", () => {
        if (sessionStorage.getItem("username") == null) {
            window.location.href = "LoginJS.html";
        } else {
            pageeditor.start(); // initialize the components
            pageeditor.refresh();
        } // display initial content
    }, false);

    function UserMessage(username, messageContainer){
        this.username=username;
        this.show=function (){
            messageContainer.textContent= this.username
        }
    } //todo edit later for Name Surname better View

    function topicShower(topiccontainerElement){
        this.topicContainer= topiccontainerElement;

        this.show=function (){
            var self= this;
            makeCall("GET", "../DownloadTopicsJS", null, function (req){
                if (req.readyState == 4 && req.status == 200){
                    var topicstoshow=JSON.parse(req.responseText);
                    if(topicstoshow.length === 0){
                        self.topicContainer.textContent="No Topics yet";
                        return;
                    }
                    for(var i=0; i<topicstoshow.length; i++){
                        printer(topicstoshow[i],self.topicContainer);
                    }
                }
            })

            var printer = function (obj, topicContainer) {
                var node = document.createTextNode(obj.id + ". " + obj.name);
                topicContainer.appendChild(node);
                topicContainer.appendChild(document.createElement("br"));
                for (var i = 0; i < obj.subtopics.length; i++) {
                    printer(obj.subtopics[i], topicContainer);
                }
            }
        }


    } //It works fine

    function addTopicForm(formId, pageEditor){
        var self=this;
        this.formContainer= formId;
        this.pageEditor= pageEditor;
        this.selector= document.getElementById("idFather");
        this.fillOption= function (){
            makeCall("GET", "../GetOptionsTopicJS", self.formContainer, function (req){
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
                var form = self.formContainer;
                if (form.checkValidity()) {
                    makeCall("POST", '../AddTopicJS', self.formContainer,
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
            //this.fillOption();
            document.getElementById("topicName").textContent="";
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

        }

        this.refresh=function (){
            addForm.reset();
        }

    }

};