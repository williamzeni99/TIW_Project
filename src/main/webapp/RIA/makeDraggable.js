

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

    /*Returns the first id free, if there is no space it returns -1*/
    function firstIDFree (id_Father){
        let x= id_Father.split('');
        let data= dataTopics;

        for (const i of x){
            data=data.subtopics[i-1];
        }
        var fatherid=data.id;
        data=data.subtopics;
        for(let i=0; i<data.length && i<9; i++){
            var newId=i+1+fatherid*10;
            if(data[i].id!=newId){
                return newId;
            }
        }

        newId=fatherid*10+data.length+1;

        if(newId%10===0){
            return -1;
        }
        else{
            return newId;
        }
    }

    function updateTree(start_id, dest_id) {
        var dest= document.getElementById(dest_id);


        if(document.getElementById(start_id)!=null){
            var newId= firstIDFree(dest_id);
            if(newId<0) return -1;
            var ul= document.createElement("ul");
            var li= document.createElement("li")
            li.textContent=newId+"."+document.getElementById(start_id).textContent.split('.')[1];
            li.id=newId;
            doDraggable(li);
            ul.appendChild(li)
            dest.appendChild(ul);
            for (let i=1; i<10; i++){
                let new_start= start_id*10+i;
                updateTree(new_start, newId);
            }
        }else{
            //document.getElementById(start_id).closest("ul").innerHTML='';
        }
    }

    /*
        The drop event is fired when an element or text selection is dropped on a valid drop target.
    */
    function drop(event){
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