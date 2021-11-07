

function makeDraggable(elements){

    for (let i = elements.length - 1; i >= 0; i--) {
        doDraggable(elements[i]);
    }

    let startE;
    let dataTopics;

    function setDataTopics(data){
        dataTopics=data;
    }

    function doDraggable(element){
        element.draggable = true;
        element.addEventListener("dragstart", dragStart); //save dragged element reference
        element.addEventListener("dragover", dragOver); // change color of reference element to red
        element.addEventListener("dragleave", dragLeave); // change color of reference element to black
        element.addEventListener("drop", drop); //change position of dragged element using the referenced element
    }
    /*
    The dragstart event is fired when the user starts
    dragging an element (if it is draggable=True)
    */
    function dragStart(event){
        startE=event.target.closest("li")
    }

    /*
        The dragover event is fired when an element
        is being dragged over a valid drop target.
     */
    function dragOver(event){
        event.preventDefault();

        var dest= event.target.closest("li");
        dest.className="selected";
    }

    /*
        The dragleave event is fired when a dragged
        element leaves a valid drop target.
    */
    function dragLeave(event){
        var dest = event.target.closest("li");

        // Mark  the current element as "notselected", then with CSS we will put it in black
        dest.className = "notselected";
    }

    /*Returns the first id free, if there is no space it returns -1, if there is not topic returns -2*/
    function firstIDFree (id_Father){
        var data=findTopic(id_Father);
        if(data===-1) return -2;
        data=data.subtopics;
        for(let i=0; i<data.length && i<9; i++){
            var newId=i+1+id_Father*10;
            if(data[i].id!=newId){
                return newId;
            }
        }

        newId=id_Father*10+data.length+1;

        if(newId%10===0){
            return -1;
        }
        else{
            return newId;
        }
    }

    /**returns the topic object. returns -1 if it doesn't find the topic in the right position*/
    function findTopic(id){
        let x= id.toString().split('');
        let data= dataTopics;

        for (const i of x){
            data=data.subtopics[i-1];
        }

        if(data.id==id){
            return data;
        }
        else{
            return -1;
        }
    }

    function getLastDigit (id) {
        let x = id.toString().split('');
        return x[x.length - 1];
    }

    function updateTree(start_id, dest_id) {
        let x= moveDest(start_id, dest_id);
        if(x<0) return x;
        let data=findTopic(Math.floor(start_id/10));
        let datas=data.subtopics;
        datas.splice(getLastDigit(start_id)-1, 1);
        reorderIds(data);
    }

    function Topic(id, name){
        this.id= id;
        this.name=name;
        this.subtopics= new Array();
    }

    function moveDest(start_id, dest_id){
        let newId= firstIDFree(dest_id);
        if(newId<0) return newId;
        let startTopic= findTopic(start_id);
        let newTopic= new Topic(newId, startTopic.name);

        let destTopic=findTopic(dest_id);
        destTopic.subtopics[getLastDigit(newTopic.id)-1]=newTopic;
        for(let i=0; i<9 && startTopic.subtopics[i]!=null; i++){
            moveDest(startTopic.subtopics[i].id, newTopic.id);
        }
    }

    /**Reorders subtopics ids*/
    function reorderIds(data){
        let subs=data.subtopics;
        for(let i=0; i<subs.length; i++){
            subs[i].id=data.id*10+i+1;
            reorderIds(subs[i]);
        }
    }

    /*
        The drop event is fired when an element or text selection is dropped on a valid drop target.
    */
    function drop(event){
        document.getElementById("errorTopicMsg").textContent="";
        event.stopImmediatePropagation();
        var dest = event.target.closest("li");
        var txt= "Do you wanna move topic "+ startE.getAttribute("id") + " into topic "+ dest.getAttribute("id")+ "?";

        if(confirm(txt)){
            reset(dest);
            setDataTopics(getDataTopics());
            let x= updateTree(startE.getAttribute("id"), dest.getAttribute("id"));
            if(x===-1){
                document.getElementById("errorTopicMsg").textContent="You cannot move this topic here. Size limit reached.";
            }
            if(x===-2){
                document.getElementById("errorTopicMsg").textContent="Somenthing graphical went wrong. ";

            }
           // startE.closest("ul").remove();
            new topicShower(document.getElementById("topics")).resetLocally();
        }
        else{
            reset(dest);
        }

    }

    function reset(element){
        element.className="notselected";
    }


}