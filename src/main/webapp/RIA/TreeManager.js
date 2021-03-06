{
    let modified=new Array();
    let dataTopics;

    this.Topic=function (id, name){
        this.id= id;
        this.name=name;
        this.subtopics= new Array(0);
    }

    function pushChange(data){
        modified.push(data);
    }

    function getChanges(){
        return modified;
    }

    function resetChanges(){
        modified=new Array();
    }

    function getDataTopics(){
        return dataTopics;
    }

    function setDataTopics(newData){
        dataTopics=newData;
    }

    /**It returns true if the destination id is a sons or it's the same of start id */
    function isInsideID(start_id, dest_id){
        let startObj=findTopic(start_id);
        if(startObj<0) return false;
        let data= new Array();
        getSons(startObj, data);
        let x= myinclude(data,dest_id);
        return x;
    }

    /**Actually it works like array.includes() but I check the correspondence with a simple ==*/
    function myinclude(array, value){
        for(const x of array){
            if (value==x) return true;
        }
        return false;
    }

    /**Fill a linear array 'data' with all the startObj sons ids*/
    function getSons(startObj, data){
        data.push(startObj.id);
        startObj=startObj.subtopics;
        for(let i=0; startObj!=null && i<startObj.length ; i++){
            getSons(startObj[i], data);
        }
    }

    /**Reorders subtopics ids*/
    function reorderIds(data) {
        let subs = data.subtopics;
        for (let i = 0; i < subs.length; i++) {
            subs[i].id = data.id * 10 + i + 1;
            reorderIds(subs[i]);
        }
    }

    /**It returns the last digit of a number*/
    function getLastDigit (id) {
        let x = id.toString().split('');
        return x[x.length - 1];
    }

    function updateTree(start_id, dest_id) {
        let startObj=findTopic(start_id);
        let destObj=findTopic(dest_id);
        let x= moveDest(startObj, destObj);
        if(x<0) return x;
        let data=findTopic(Math.floor(parseInt(startObj.id)/10)); //todo check this part
        let dataX=data.subtopics;
        dataX.splice(getLastDigit(startObj.id)-1, 1);
        reorderIds(data);
        pushChange({start_id,dest_id});
    }

    function moveDest(startObj, destObj){
        let newId= firstIDFree(destObj.id);
        if(newId<0) return newId;

        let newTopic= new Topic(newId, startObj.name);
        destObj.subtopics[getLastDigit(newTopic.id)-1]=newTopic;
        for(let i=0; i<9 && startObj.subtopics[i]!=null; i++){
            moveDest(startObj.subtopics[i], newTopic);
        }
    }

    /**It returns the first id free, if there is no space it returns -1, if there is not topic returns -2*/
    function firstIDFree (id_Father){
        let data=findTopic(id_Father);
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

    /**It returns the topic object with their sub-links. It returns -1 if it doesn't find the topic in the right position*/
    function findTopic(id){
        let data= dataTopics;
        if(id==0) return data;
        let x= id.toString().split('');

        for (const i of x){
            data=data.subtopics[i-1];
        }

        if(data==null) return -1; //todo check this part
        if(data.id==id) return data;

    }


}