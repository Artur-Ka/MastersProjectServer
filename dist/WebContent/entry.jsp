<!DOCTYPE html>
<html>
	<title>Онлайн поликлиника</title>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache" />
		<link rel="shortcut icon" href="./images/favicon.png" type="image/png">
		
		<link rel="stylesheet" href="./css/style.css">
		<link rel="stylesheet" type="text/css" href="./css/presentationCycle.css" />
		
		<!-- Scripts -->
		<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js?ver=1.3.2'></script>
		<script type='text/javascript' src='./js/jquery.cycle.js'></script>
		<script type='text/javascript' src='./js/presentationCycle.js'></script>
	</head>
	<body>
		<!-- Хедер -->
		<div id="header_wrapper">
			<div id="header"></div>
		</div>
		<!-- Конец хедера -->
		<!-- Банер (шапка) -->
		<div id="banner_wrapper">
			<div id="banner"></div>
		</div>
		<!-- Конец банера -->
		<!-- Меню -->
		<div id="menu_wrapper">    
			<div id="menu">
				<ul>
					<li><a href="http://127.0.0.1:8080/index.html"><span></span>Головна</a></li>
					<li><a href="http://127.0.0.1:8080/doctors.html"><span></span>Лікарі</a></li>
					<li><a href="http://127.0.0.1:8080/entry.jsp"><span></span>Запис</a></li>
					<li><a href="http://127.0.0.1:8080/info.html" class="current"><span></span>Про нас</a></li>
					<li><a href="http://127.0.0.1:8080/gallery.html"><span></span>Фото</a></li>
					<li><a href="http://127.0.0.1:8080/contacts.html"><span></span>Контакти</a></li>
				</ul>    	
			</div>
		</div>
		<!-- Конец меню -->
		<!-- Контент -->
		<%
			final String login;
			final String password;
		%>
		<!-- Конец контента -->
		<!-- Футер -->
		<div id="footer_wrapper">        
			<div id="footer">Copyright © 2017 <a href="subpage.html">Онлайн поліклініка "OnClinic"</a></div>
		</div>
		<!-- Конец футера -->
	</body>
</html>