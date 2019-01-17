<html>
<#include "header.html">
<body onload="connect()">

<div class="jumbotron">
    <div class="row justify-content-center">
        <div class="col-md-10"><p class="text-center">E-Purse starting</p></div>
    </div>
    <div class="row justify-content-center">
        <div class="col-md-5">
            <p align="center">
                <button class="btn btn-primary"
                        data-toggle="modal" data-target="#incomeModal"
                        type="button">Income
                </button>
            </p>
        </div>
        <div class="col-md-5">

            <button id="disconnect" class="btn btn-primary"
                    type="button"
                    data-toggle="modal" data-target="#withdrawalForm">Withdrawal
            </button>

        </div>
    </div>
</div>


<div class="modal fade" id="incomeModal" role="dialog" aria-labelledby="incomeModalLabel">

    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title" id="incomeModalLabel">Income:</h3>
            </div>

            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="trans-date" class="col-form-label">Date:</label>
                        <input type="date" class="form-control" id="trans-date">
                    </div>
                    <div class="form-group">
                        <label for="trans-amount" class="col-form-label">Amount:</label>
                        <input type="number" class="form-control" id="trans-amount">
                    </div>
                    <div class="form-group">
                        <label for="trans-comment" class="col-form-label">Comment:</label>
                        <textarea class="form-control" id="trans-comment"></textarea>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="income()">Save</button>
            </div>

        </div>
    </div>


</div>


</body>
</html>