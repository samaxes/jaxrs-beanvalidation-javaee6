<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>JAX-RS Bean Validation I18N</title>
    <!-- CSS -->
    <link rel="stylesheet" href="style.css" />
    <style>
        section {
            background: #f6f6f6;
            margin-bottom: 1em;
            padding: 1em;
        }
    </style>
</head>
<body>
    <header>
        <h1>JAX-RS Bean Validation Error Message Internationalization</h1>
    </header>
    <main>
        <section>
            <a href="${pageContext.request.contextPath}/r/persons">Get all</a>
        </section>
        <section>
            <form action="${pageContext.request.contextPath}/r/persons" method="get">
                Person id: <input type="text" id="personId" name="id" /><br />
                <input type="button" id="getPerson" value="Get person" />
            </form>
        </section>
        <section>
            <form action="${pageContext.request.contextPath}/r/persons/create" method="post">
                Person id: <input type="text" name="id" /><br />
                Person name: <input type="text" name="name" /><br />
                <input type="submit" value="Create person" />
            </form>
        </section>
    </main>
    <footer>
        <aside>By <a href="http://www.samaxes.com/">Samuel Santos</a></aside>
    </footer>
    <script type="text/javascript">
        document.getElementById('getPerson').addEventListener('click', function(event) {
            location.href = document.forms[0].action + '/' + document.getElementById('personId').value;
        }, false);
    </script>
</body>
</html>
